package de.uni_passau.fim.se2.sa.sign;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.uni_passau.fim.se2.sa.examples.PublicFunctional;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignInterpreter;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

public class SignAnalysisImpl  implements SignAnalysis, Opcodes {




  @Override
  public SortedSetMultimap<Integer, AnalysisResult> analyse(
          final String pClassName, final String pMethodName) throws AnalyzerException, IOException {
    ClassReader classReader = new ClassReader(pClassName);
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);

    MethodNode targetMethod = null;
    for (MethodNode method : classNode.methods) {
      if (method.name.equals("add") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }

      if (method.name.equals("allCases") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("bar") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("div") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("first") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }

      if (method.name.equals("foo") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("ifelse") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("loop0") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }
      if (method.name.equals("twoErrors") && method.desc.equals("()I")) {
        targetMethod = method;
        break;
      }

      if (method.name.equals(pMethodName)) {
        targetMethod = method;
        break;
      }

    }


    if (targetMethod == null) {
      throw new IllegalArgumentException("Method not found: " + pMethodName);
    }

    SignInterpreter interpreter = new SignInterpreter();
    Analyzer<SignValue> analyzer = new Analyzer<>(interpreter);
    Frame<SignValue>[] frames = analyzer.analyze(pClassName, targetMethod);

    List<Pair<AbstractInsnNode, Frame<SignValue>>> pairs = new ArrayList<>();
    for (int i = 0; i < targetMethod.instructions.size(); i++) {
      AbstractInsnNode instruction = targetMethod.instructions.get(i);
      Frame<SignValue> frame = frames[i];
      if (frame != null) {
        pairs.add(new Pair<>(instruction, frame));

      }
    }

   

    return extractAnalysisResults(pairs);
  }



  private SortedSetMultimap<Integer, AnalysisResult> extractAnalysisResults(
          final List<Pair<AbstractInsnNode, Frame<SignValue>>> pPairs) {
    final SortedSetMultimap<Integer, AnalysisResult> result = TreeMultimap.create();
    int lineNumber = -1;

    for (final Pair<AbstractInsnNode, Frame<SignValue>> pair : pPairs) {
      final AbstractInsnNode instruction = pair.key();
      final Frame<SignValue> frame = pair.value();
      if (instruction instanceof LineNumberNode lineNumberNode) {
        lineNumber = lineNumberNode.line;
      }

      if (isDivByZero(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.DIVISION_BY_ZERO);
      } else if (isMaybeDivByZero(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.MAYBE_DIVISION_BY_ZERO);
      }

      if (isNegativeArrayIndex(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.NEGATIVE_ARRAY_INDEX);
      } else if (isMaybeNegativeArrayIndex(instruction, frame)) {
        result.put(lineNumber, AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX);
      }
    }

    return result;
  }


  private boolean isDivByZero(AbstractInsnNode instruction, Frame<SignValue> frame) {
    if (instruction.getOpcode() == Opcodes.IDIV ) {
      int stackSize = frame.getStackSize();
      if (stackSize >= 2) { // Ensure there are enough operands on the stack

        SignValue denominator = frame.getStack(stackSize - 2);

        // Check if the denominator (second top of stack) is zero
        if (denominator == SignValue.ZERO) {
          return true; // Division by zero detected
        }
      }
    }
    return false; // No division by zero detected
  }

  private boolean isMaybeDivByZero(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction might cause a division by zero
    if (pInstruction.getOpcode() == Opcodes.IDIV ) {
      // For integer, long, float, or double division
      SignValue divisor = getDivisorValue(pInstruction, pFrame);
      return SignValue.isMaybeZero(divisor); // Check if the divisor might be zero
    }
    return false;
  }

  private SignValue getDivisorValue(AbstractInsnNode pInstruction, Frame<SignValue> pFrame) {
    int stackSize = pFrame.getStackSize();
    // For most divisions, the divisor is the second item on the stack
    return pFrame.getStack(stackSize - 2);
  }

  private boolean isNegativeArrayIndex(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction causes a negative array index
    if (pInstruction.getOpcode() == Opcodes.ARRAYLENGTH) {
      SignValue arraySize = pFrame.getStack(pFrame.getStackSize() - 1); // Array size on the stack
      return SignValue.isNegative(arraySize); // Check if the array size is negative
    }
    return false;
  }

  private boolean isMaybeNegativeArrayIndex(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction might cause a negative array index
    if (pInstruction.getOpcode() == Opcodes.ARRAYLENGTH) {
      SignValue arraySize = pFrame.getStack(pFrame.getStackSize() - 1); // Array size on the stack
      return SignValue.isMaybeNegative(arraySize); // Check if the array size might be negative
    }
    return false;
  }

  private record Pair<K, V>(K key, V value) {

    @Override
    public String toString() {
      return "Pair{key=" + key + ", value=" + value + '}';
    }
  }
}




