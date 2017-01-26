package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class Point2FTests {

  private static final boolean output = true;

  private static final double DELTA = 1e-15;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Point2F_ {

      public static class positive_tests {

        @Test
        public void zeroed() {
          Point2F p = new Point2F();
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Point2F_float_float {

      public static class positive_tests {

        @Test
        public void non_zero() {
          Point2F p = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals((float) PRIMES[0], (float) PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Point2F_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F((Point2F) null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Point2F src = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("src:" + src);
          Point2F p = new Point2F(src);
          if (output) System.out.println("p:" + p);
          Assert.assertEquals(src, p);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Point2F_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F((Point2) null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Point2 src = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          Point2F p = new Point2F(src);
          if (output) System.out.println("p:" + p);
          Assert.assertEquals(src, p);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setX {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Point2F p = new Point2F() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("p:" + p);
        final float testValue = 0;
        p.setX(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getX(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Point2F p = new Point2F() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("p:" + p);
        final float testValue = 1;
        p.setX(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getX(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setY {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Point2F p = new Point2F() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("p:" + p);
        final float testValue = 0;
        p.setY(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getY(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Point2F p = new Point2F() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("p:" + p);
        final float testValue = 1;
        p.setY(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getY(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_float_float {

      public static class positive_tests {

        @Test
        public void unchanged() {
          Point2F p = new Point2F() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("p:" + p);
          final float testValue = 0;
          p.set(testValue, testValue);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final float[] calls = {0};
          Point2F p = new Point2F() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("p:" + p);
          final float testValue = 1;
          p.set(testValue, testValue);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0], DELTA);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F();
          if (output) System.out.println("p:" + p);
          p.set((Point2F) null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Point2F src = new Point2F();
          Point2F p = new Point2F() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("src:" + p);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(src));
        }

        @Test
        public void changed() {
          final boolean[] changed = {false};
          final float[] calls = {0};
          Point2F src = new Point2F(1, 1);
          Point2F p = new Point2F() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("src:" + p);
          if (output) System.out.println("p:" + p);
          p.set(src);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(src));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0], DELTA);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class onChange {

    public static class positive_tests {

      @Test
      public void onChange() {
        Point2F p = new Point2F();
        p.onChange();
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class equals {

    @RunWith(Enclosed.class)
    public static class equals_float_float {

      public static class positive_tests {

        @Test
        public void _true() {
          Point2F p = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals((float) PRIMES[0], (float) PRIMES[1]));
        }

        @Test
        public void _false() {
          Point2F p = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertFalse(p.equals(0, (float) PRIMES[1]));
          Assert.assertFalse(p.equals((float) PRIMES[0], 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class equals_Object {

      public static class positive_tests {

        @Test
        public void reflexive() {
          Point2F x = new Point2F((float) PRIMES[0] + 0.1f, (float) PRIMES[1] + 0.2f);
          if (output) System.out.println("x:" + x);
          Assert.assertTrue(x.equals(x));
          Assert.assertEquals(x.hashCode(), x.hashCode());
        }

        @Test
        public void symmetric() {
          Point2F x = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          Point2F y = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          Assert.assertTrue(x.equals(y) && y.equals(x));
          Assert.assertEquals(x.hashCode(), y.hashCode());
        }

        @Test
        public void transitive() {
          Point2F x = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          Point2F y = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          Point2F z = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          if (output) System.out.println("z:" + z);
          Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z));
        }

        @Test
        public void nullity() {
          Point2F x = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(null));
        }

        @Test
        public void type() {
          Point2F x = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(new Object()));
        }

        @Test
        public void Point2FF() {
          Point2 p1 = new Point2(PRIMES[0], PRIMES[1]);
          Point2F p2 = new Point2F((float) (float) PRIMES[0], (float) (float) PRIMES[1]);
          if (output) System.out.println("p1:" + p1);
          if (output) System.out.println("p2:" + p2);
          Assert.assertEquals(p1, p2);
          Assert.assertEquals(p1.hashCode(), p2.hashCode());
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toString {

    public static class positive_tests {

      @Test
      public void nullity() {
        Point2F p = new Point2F();
        String toString = p.toString();
        if (output) System.out.println("p:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }
  
}
