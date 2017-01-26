package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class ImmutablePoint2Tests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class FactoryMethods {

    @RunWith(Enclosed.class)
    public static class newImmutablePoint2_ {

      public static class positive_tests {

        @Test
        public void empty() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class newImmutablePoint2_int_int {

      public static class positive_tests {

        @Test
        public void non_empty() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(PRIMES[0], PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class copyOf_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          ImmutablePoint2 p = ImmutablePoint2.copyOf(null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() throws NoSuchFieldException {
          Point2 src = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          ImmutablePoint2 p = ImmutablePoint2.copyOf(src);
          if (output) System.out.println("p:" + src);
          Assert.assertEquals(src, p);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setX {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
        if (output) System.out.println("p:" + p);
        p.setX(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setY {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
        if (output) System.out.println("p:" + p);
        p.setY(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_int_int {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          p.set(0, 0);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("src:" + p);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

  }

}
