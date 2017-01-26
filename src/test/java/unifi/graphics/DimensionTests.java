package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class DimensionTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Dimension_ {

      public static class positive_tests {

        @Test
        public void zeroed() {
          Dimension d = new Dimension();
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Dimension_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_width() {
          Dimension d = new Dimension(-1, 0);
          if (output) System.out.println("d:" + d);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_height() {
          Dimension d = new Dimension(0, -1);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void non_zero() {
          Dimension d = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(PRIMES[0], PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Dimension_Dimension {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Dimension d = new Dimension(null);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Dimension src = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          Dimension d = new Dimension(src);
          if (output) System.out.println("d:" + d);
          Assert.assertEquals(src, d);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setWidth {

    public static class negative_tests {

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_width() {
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("d:" + d);
        d.setWidth(-1);
        if (output) System.out.println("d:" + d);
      }

    }

    public static class positive_tests {

      @Test
      public void unchanged() {
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("d:" + d);
        final int testValue = 0;
        d.setWidth(testValue);
        if (output) System.out.println("d:" + d);
        Assert.assertEquals(testValue, d.getWidth());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("d:" + d);
        final int testValue = 1;
        d.setWidth(testValue);
        if (output) System.out.println("d:" + d);
        Assert.assertEquals(testValue, d.getWidth());
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setHeight {

    public static class negative_tests {

      @Test(expected = IllegalArgumentException.class)
      public void fails_invalid_width() {
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("d:" + d);
        d.setHeight(-1);
        if (output) System.out.println("d:" + d);
      }

    }

    public static class positive_tests {

      @Test
      public void unchanged() {
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("d:" + d);
        final int testValue = 0;
        d.setHeight(testValue);
        if (output) System.out.println("d:" + d);
        Assert.assertEquals(testValue, d.getHeight());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Dimension d = new Dimension() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("d:" + d);
        final int testValue = 1;
        d.setHeight(testValue);
        if (output) System.out.println("d:" + d);
        Assert.assertEquals(testValue, d.getHeight());
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_width() {
          Dimension d = new Dimension();
          if (output) System.out.println("d:" + d);
          d.set(-1, 0);
          if (output) System.out.println("d:" + d);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_height() {
          Dimension d = new Dimension();
          if (output) System.out.println("d:" + d);
          d.set(0, -1);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Dimension d = new Dimension() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("d:" + d);
          final int testValue = 0;
          d.set(testValue, testValue);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Dimension d = new Dimension() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("d:" + d);
          final int testValue = 1;
          d.set(testValue, testValue);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Dimension {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Dimension d = new Dimension();
          if (output) System.out.println("d:" + d);
          d.set(null);
          if (output) System.out.println("d:" + d);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Dimension src = new Dimension();
          Dimension d = new Dimension() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("src:" + d);
          if (output) System.out.println("d:" + d);
          d.set(src);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(src));
        }

        @Test
        public void changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Dimension src = new Dimension(1, 1);
          Dimension d = new Dimension() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("src:" + d);
          if (output) System.out.println("d:" + d);
          d.set(src);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(src));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class onChange {

    public static class positive_tests {

      @Test
      public void onChange() {
        Dimension d = new Dimension();
        d.onChange();
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class equals {

    @RunWith(Enclosed.class)
    public static class equals_int_int {

      public static class positive_tests {

        @Test
        public void _true() {
          Dimension d = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("d:" + d);
          Assert.assertTrue(d.equals(PRIMES[0], PRIMES[1]));
        }

        @Test
        public void _false() {
          Dimension d = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("d:" + d);
          Assert.assertFalse(d.equals(0, PRIMES[1]));
          Assert.assertFalse(d.equals(PRIMES[0], 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class equals_Object {

      public static class positive_tests {

        @Test
        public void reflexive() {
          Dimension x = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertTrue(x.equals(x));
          Assert.assertEquals(x.hashCode(), x.hashCode());
        }

        @Test
        public void symmetric() {
          Dimension x = new Dimension(PRIMES[0], PRIMES[1]);
          Dimension y = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          Assert.assertTrue(x.equals(y) && y.equals(x));
          Assert.assertEquals(x.hashCode(), y.hashCode());
        }

        @Test
        public void transitive() {
          Dimension x = new Dimension(PRIMES[0], PRIMES[1]);
          Dimension y = new Dimension(PRIMES[0], PRIMES[1]);
          Dimension z = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          if (output) System.out.println("z:" + z);
          Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z));
        }

        @Test
        public void nullity() {
          Dimension x = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(null));
        }

        @Test
        public void type() {
          Dimension x = new Dimension(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(new Object()));
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toString {

    public static class positive_tests {

      @Test
      public void nullity() {
        Dimension p = new Dimension();
        String toString = p.toString();
        if (output) System.out.println("p:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

}
