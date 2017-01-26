package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class ImmutablePoint2FTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class FactoryMethods {

    @RunWith(Enclosed.class)
    public static class newImmutablePoint2F_ {

      public static class positive_tests {

        @Test
        public void empty() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class newImmutablePoint2F_float_float {

      public static class positive_tests {

        @Test
        public void non_empty() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals((float) PRIMES[0], (float) PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class copyOf_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          ImmutablePoint2F p = ImmutablePoint2F.copyOf((Point2F) null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() throws NoSuchFieldException {
          Point2F src = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("src:" + src);
          ImmutablePoint2F p = ImmutablePoint2F.copyOf(src);
          if (output) System.out.println("p:" + src);
          Assert.assertEquals(src, p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class copyOf_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          ImmutablePoint2F p = ImmutablePoint2F.copyOf((Point2) null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() throws NoSuchFieldException {
          Point2 src = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          ImmutablePoint2F p = ImmutablePoint2F.copyOf(src);
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
        ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
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
        ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
        if (output) System.out.println("p:" + p);
        p.setY(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_float_float {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          p.set(0, 0);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + p);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + p);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

  }

}
