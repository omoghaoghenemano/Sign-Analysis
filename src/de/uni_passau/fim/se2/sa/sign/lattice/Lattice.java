package de.uni_passau.fim.se2.sa.sign.lattice;

/**
 * A lattice type that is parametric in its element types.
 *
 * @param <E> The element type of the lattice
 */
public interface Lattice<E> {

  /**
   * The top element.
   *
   * @return A lattice element for TOP
   */
  E top();

  /**
   * The bottom element.
   *
   * @return A lattice element for BOTTOM
   */
  E bottom();

  /**
   * Joins two lattice elements according to their position in the lattice.
   *
   * @param pFirst The first element
   * @param pSecond The second element
   * @return The join of the two elements
   */
  E join(final E pFirst, final E pSecond);

  /**
   * Whether the first element is less or equal than the second element according to the lattice.
   *
   * @param pFirst The first element
   * @param pSecond The second element
   * @return Whether the first element is less or equal
   */
  boolean isLessOrEqual(final E pFirst, final E pSecond);
}
