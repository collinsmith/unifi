package unifi.graphics.old;

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

  @RunWith(Enclosed.class)
  public static class add {

    @RunWith(Enclosed.class)
    public static class add_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.add(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2F_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.add(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.add(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.add(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.add(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.add(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class subtract {

    @RunWith(Enclosed.class)
    public static class subtract_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.subtract(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2F_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.subtract(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2F src = new Point2F();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.subtract(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2 {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("p:" + p);
          p.subtract(src);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.subtract(src, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          Point2 src = new Point2();
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.subtract(src, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class scale {

    @RunWith(Enclosed.class)
    public static class scale_double {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          final double scalar = Math.PI;
          p.scale(scalar);
          if (output) System.out.println("p:" + p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class scale_double_Point2F {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          if (output) System.out.println("p:" + p);
          final double scalar = Math.PI;
          Point2F result = p.scale(scalar, p);
          if (output) System.out.println("result:" + result);
        }

        @Test
        public void supported() {
          ImmutablePoint2F p = ImmutablePoint2F.newImmutablePoint2F();
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          final double scalar = Math.PI;
          Point2F result = p.scale(scalar, dst);
          if (output) System.out.println("result:" + result);
        }

      }

    }

  }

}
