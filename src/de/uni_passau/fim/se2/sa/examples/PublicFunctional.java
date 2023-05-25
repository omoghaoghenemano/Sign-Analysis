package de.uni_passau.fim.se2.sa.examples;

import java.util.ArrayList;
import java.util.List;

public class PublicFunctional {

  public int allCases() {
    int zero = 0;
    int spos = 1;
    int sneg = -1;
    int nzero = spos * sneg;
    int pos;
    int neg;
    if (zero == 0) {
      pos = spos;
      neg = sneg;
    } else {
      pos = zero;
      neg = zero;
    }
    int top = pos - neg;

    int res1 = 1 / zero;
    int res2 = 1 / pos;
    int res3 = 1 / neg;
    int res4 = 1 / top;

    int[] array = {0, 1, 2};
    int res5 = array[sneg];
    int res6 = array[neg];
    int res7 = array[top];

    double b = pos / neg;
    return 0;
  }

  public void twoErrors() {
    int[] arr = {0, 1, 2};
    int a = 0;
    int b = -1;
    int c = arr[b] / a;
  }

  public int ifelse() {
    int a = -20;
    int b;
    if (a == 2) {
      b = 0;
    } else {
      b = 1;
    }
    int c = a / b;
    return c;
  }

  public void loop0() {
    for (int i = 5; i > 0; --i) {
      int a = 1 / i;
    }
  }

  public int add() {
    int a = 1;
    int b = 2;
    int c = a + b;
    return c;
  }

  public int div() {
    int a = 1;
    int b = 0;
    int c = a / b;
    return c;
  }

  public int foo() {
    int a = -1;
    int[] arr = {0, 1, 2};
    return arr[a];
  }

  public int bar() {
    boolean foo = false;
    int[] arr = {0, 1, 2};
    int a;
    if (foo) {
      a = 1;
    } else {
      a = -2;
    }
    return arr[a];
  }

  public int first() {
    int a = 2;
    int b = second(a);
    return b;
  }

  public int second(int x) {
    return 2 * x;
  }

  public int listSize() {
    List<String> list = new ArrayList<>();
    int x = 42 * list.size();
    return x;
  }
}
