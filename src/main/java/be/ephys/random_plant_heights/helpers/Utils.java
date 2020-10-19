package be.ephys.random_plant_heights.helpers;

import java.util.Random;

public final class Utils {

  public static int randomIntInclusive(Random random, int min, int max) {
    max = max + 1;
    return random.nextInt(max - min) + min;
  }
}
