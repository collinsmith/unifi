package unifi.graphics.old;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import unifi.graphics.old.ImmutableRect;
import unifi.graphics.old.Rect;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class ImmutableRectTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class FactoryMethods {

    @RunWith(Enclosed.class)
    public static class newImmutableRect_ {

      public static class positive_tests {

        @Test
        public void empty() {
          ImmutableRect r = ImmutableRect.newImmutableRect();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class newImmutableRect_int_int_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_left_greater_than_right() {
          ImmutableRect r = ImmutableRect.newImmutableRect(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          ImmutableRect r = ImmutableRect.newImmutableRect(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_empty() {
          ImmutableRect r = ImmutableRect.newImmutableRect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class copyOf_Rect {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          ImmutableRect r = ImmutableRect.copyOf(null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() throws NoSuchFieldException {
          Rect src = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("src:" + src);
          ImmutableRect r = ImmutableRect.copyOf(src);
          if (output) System.out.println("r:" + src);
          Assert.assertEquals(src, r);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setLeft {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableRect r = ImmutableRect.newImmutableRect();
        if (output) System.out.println("r:" + r);
        r.setLeft(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setTop {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableRect r = ImmutableRect.newImmutableRect();
        if (output) System.out.println("r:" + r);
        r.setTop(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setRight {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableRect r = ImmutableRect.newImmutableRect();
        if (output) System.out.println("r:" + r);
        r.setRight(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setBottom {

    public static class positive_tests {

      @Test(expected = UnsupportedOperationException.class)
      public void unsupported() {
        ImmutableRect r = ImmutableRect.newImmutableRect();
        if (output) System.out.println("r:" + r);
        r.setBottom(0);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_int_int_int_int {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          ImmutableRect r = ImmutableRect.newImmutableRect();
          if (output) System.out.println("r:" + r);
          r.set(0, 0, 0, 0);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Rect {

      public static class positive_tests {

        @Test(expected = UnsupportedOperationException.class)
        public void unsupported() {
          Rect src = new Rect();
          ImmutableRect r = ImmutableRect.newImmutableRect();
          if (output) System.out.println("src:" + r);
          if (output) System.out.println("r:" + r);
          r.set(src);
          if (output) System.out.println("r:" + r);
        }

      }

    }

  }

}
