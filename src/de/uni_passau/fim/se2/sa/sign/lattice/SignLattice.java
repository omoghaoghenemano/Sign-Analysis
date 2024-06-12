package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;

/** A lattice for {@link SignValue}. */
public class SignLattice implements Lattice<SignValue> {

  /** {@inheritDoc} */
  @Override
  public SignValue top() {
    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue bottom() {
    return SignValue.BOTTOM;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue join(final SignValue pFirst, final SignValue pSecond) {
    return pFirst.join(pSecond);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isLessOrEqual(final SignValue pFirst, final SignValue pSecond) {
    return pFirst.isLessOrEqual(pSecond);
  }
}
