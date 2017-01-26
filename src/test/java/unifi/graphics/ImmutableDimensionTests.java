package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class ImmutableDimensionTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class FactoryMethods {

    @RunWith(Enclosed.class)
    public static class newImmutableDimension_ {

      public static class positive_tests {

        @Test
        public void empty() {
          ImmutableDimension d = ImmutableDimension.newImmutableDimension();
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class newImmutableDimension_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_width() {
          ImmutableDimension d = ImmutableDimension.newImmutableDimension(-1, 0);
          if (output) System.out.println("d:" + d);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_height() {
          ImmutableDimension d = ImmutableDimension.newImmutableDimension(0, -1);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void non_empty() {
          ImmutableDimension d = ImmutableDimension.newImmutableDimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(PRIMES[0], PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class copyOf_Dimension {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          ImmutableDimension d = ImmutableDimension.copyOf(null);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() throws NoSuchFieldException {
          Dimension src = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          ImmutableDimension d = ImmutableDimension.copyOf(src);
          if (output) System.out.println("d:" + src);
          Assert.assertEquals(src, d);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setWidth {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableDimension d = ImmutableDimension.newImmutableDimension();
        if (output) System.out.println("d:" + d);
        d.setWidth(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setHeight {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableDimension d = ImmutableDimension.newImmutableDimension();
        if (output) System.out.println("d:" + d);
        d.setHeight(0);
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
          ImmutableDimension d = ImmutableDimension.newImmutableDimension();
          if (output) System.out.println("d:" + d);
          d.set(0, 0);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Dimension {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Dimension src = new Dimension();
          ImmutableDimension d = ImmutableDimension.newImmutableDimension();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("d:" + d);
          d.set(src);
          if (output) System.out.println("d:" + d);
        }

      }

    }

  }

}
