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




  public String add(List<Pair<AbstractInsnNode, Frame<SignValue>>> elements) {
    if (elements == null || elements.isEmpty()) {
      return "No warnings or errors found";
    }

    StringBuilder result = new StringBuilder();
    int lineNumber = -1;

    for (Pair<AbstractInsnNode, Frame<SignValue>> pair : elements) {
      AbstractInsnNode instruction = pair.key();
      Frame<SignValue> frame = pair.value();

      if (instruction instanceof LineNumberNode) {
        lineNumber = ((LineNumberNode) instruction).line;
      }

      if (isDivByZero(instruction, frame)) {
        result.append("Line ").append(lineNumber).append(": ").append(AnalysisResult.DIVISION_BY_ZERO.getValue()).append("\n");
      } else if (isMaybeDivByZero(instruction, frame)) {
        result.append("Line ").append(lineNumber).append(": ").append(AnalysisResult.MAYBE_DIVISION_BY_ZERO.getValue()).append("\n");
      }

      if (isNegativeArrayIndex(instruction, frame)) {
        result.append("Line ").append(lineNumber).append(": ").append(AnalysisResult.NEGATIVE_ARRAY_INDEX.getValue()).append("\n");
      } else if (isMaybeNegativeArrayIndex(instruction, frame)) {
        result.append("Line ").append(lineNumber).append(": ").append(AnalysisResult.MAYBE_NEGATIVE_ARRAY_INDEX.getValue()).append("\n");
      }

      if (isAddition(instruction, frame)) {
        result.append("Line ").append(lineNumber).append(": INFO: Addition operation\n");
      }
    }

    return result.toString().trim(); // Remove the trailing newline
  }





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

  private boolean isDivByZero(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction is a division or remainder operation
    if (pInstruction.getOpcode() == IDIV ){
      // Get the value on the stack that would be the divisor
      SignValue divisor = pFrame.getStack(pFrame.getStackSize() - 1);
      return divisor == SignValue.ZERO;
    }
    return false;
  }

  private boolean isMaybeDivByZero(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction is a division or remainder operation
    if (pInstruction.getOpcode() == IDIV ){
      SignValue divisor = pFrame.getStack(pFrame.getStackSize() - 1);
      return divisor == SignValue.ZERO || divisor == SignValue.ZERO_MINUS || divisor == SignValue.ZERO_PLUS;
    }
    return false;
  }

  private boolean isNegativeArrayIndex(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction is an array load or store operation
    if (pInstruction.getOpcode() == IALOAD ){
      // Get the index on the stack
      SignValue index = pFrame.getStack(pFrame.getStackSize() - 1);
      return index == SignValue.MINUS || index == SignValue.ZERO_MINUS || index == SignValue.PLUS_MINUS;
    }
    return false;
  }

  private boolean isMaybeNegativeArrayIndex(
          final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction is an array load or store operation
    if (pInstruction.getOpcode() == IALOAD ){
      // Get the index on the stack
      SignValue index = pFrame.getStack(pFrame.getStackSize() - 1);
      return index == SignValue.MINUS || index == SignValue.ZERO_MINUS || index == SignValue.PLUS_MINUS || index == SignValue.TOP;
    }
    return false;
  }
  private boolean isAddition(final AbstractInsnNode pInstruction, final Frame<SignValue> pFrame) {
    // Check if the instruction is an addition operation
    if (pInstruction.getOpcode() == IADD || pInstruction.getOpcode() == FADD
            || pInstruction.getOpcode() == DADD || pInstruction.getOpcode() == LADD) {
      return true;
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
