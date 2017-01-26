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
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class add {

    @RunWith(Enclosed.class)
    public static class add_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.add(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2 result = p.add(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          Point2 dst = new Point2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2 result = p.add(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class subtract {

    @RunWith(Enclosed.class)
    public static class subtract_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.subtract(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2 result = p.subtract(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2 src = new Point2();
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          Point2 dst = new Point2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2 result = p.subtract(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class scale {

    @RunWith(Enclosed.class)
    public static class scale_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          final double scalar = Math.PI;
          p.scale(scalar);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class scale_Point2_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          if (output) System.out.println("p:" + p);
          final double scalar = Math.PI;
          Point2 result = p.scale(scalar, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          ImmutablePoint2 p = ImmutablePoint2.newImmutablePoint2();
          Point2 dst = new Point2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          final double scalar = Math.PI;
          Point2 result = p.scale(scalar, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

}
