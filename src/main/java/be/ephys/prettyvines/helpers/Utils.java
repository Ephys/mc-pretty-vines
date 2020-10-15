package be.ephys.prettyvines.helpers;

import java.util.Random;

public final class Utils {

  public static int randomIntInclusive(Random random, int min, int max) {
    max = max + 1;
    return random.nextInt(max - min) + min;
  }
}
