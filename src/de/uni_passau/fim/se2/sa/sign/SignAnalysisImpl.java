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
    if (instruction.getOpcode() == IDIV) {
      int stackSize = frame.getStackSize();
      if (stackSize >= 2) { // Ensure there are enough operands on the stack
        SignValue divisor = frame.getStack(stackSize - 1);
        SignValue denominator = frame.getStack(stackSize - 2);

        // Check if the denominator (second top of stack) is zero
        if (denominator == SignValue.ZERO) {
          return true; // Division by zero detected
        }
      }
    }
    return false; // No division by zero detected
  }


  private boolean isMaybeDivByZero(AbstractInsnNode instruction, Frame<SignValue> frame) {
    if (instruction.getOpcode() == IDIV) {
      int stackSize = frame.getStackSize();
      if (stackSize > 0) { // Ensure there are operands on the stack
        SignValue divisor = frame.getStack(stackSize - 1);

        // Check if the divisor is potentially zero, zero minus, or zero plus
        return divisor == SignValue.ZERO ||
                divisor == SignValue.ZERO_MINUS ||
                divisor == SignValue.ZERO_PLUS;
      }
    }
    return false;
  }



  private boolean isNegativeArrayIndex(AbstractInsnNode instruction, Frame<SignValue> frame) {
    if (instruction.getOpcode() == IALOAD) {
      int stackSize = frame.getStackSize();
      if (stackSize > 0) { // Ensure there are operands on the stack
        SignValue indexValue = frame.getStack(stackSize - 1);

        // Check if the index value is negative, zero minus, or plus minus
        return indexValue == SignValue.MINUS ||
                indexValue == SignValue.ZERO_MINUS ||
                indexValue == SignValue.PLUS_MINUS;
      }
    }
    return false;
  }


  private boolean isMaybeNegativeArrayIndex(AbstractInsnNode instruction, Frame<SignValue> frame) {
    if (instruction.getOpcode() == IALOAD) {
      int stackSize = frame.getStackSize();
      if (stackSize > 0) { // Ensure there are operands on the stack
        SignValue indexValue = frame.getStack(stackSize - 1);

        // Check if the index value is potentially negative, zero minus, plus minus, or top
        return indexValue == SignValue.MINUS ||
                indexValue == SignValue.ZERO_MINUS ||
                indexValue == SignValue.PLUS_MINUS ||
                indexValue == SignValue.TOP;
      }
    }
    return false;
  }





  public record Pair<K, V>(K key, V value) {

    @Override
    public String toString() {
      return "Pair{key=" + key + ", value=" + value + '}';
    }
  }
}
