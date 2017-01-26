package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class Point2Tests {

  private static final boolean output = true;

  private static final double DELTA = 1e-15;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Point2_ {

      public static class positive_tests {

        @Test
        public void zeroed() {
          Point2 p = new Point2();
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Point2_int_int {

      public static class positive_tests {

        @Test
        public void non_zero() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(PRIMES[0], PRIMES[1]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Point2_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2 p = new Point2(null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Point2 src = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("src:" + src);
          Point2 p = new Point2(src);
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
        Point2 p = new Point2() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("p:" + p);
        final int testValue = 0;
        p.setX(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getX());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Point2 p = new Point2() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("p:" + p);
        final int testValue = 1;
        p.setX(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getX());
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setY {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Point2 p = new Point2() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("p:" + p);
        final int testValue = 0;
        p.setY(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getY());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Point2 p = new Point2() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("p:" + p);
        final int testValue = 1;
        p.setY(testValue);
        if (output) System.out.println("p:" + p);
        Assert.assertEquals(testValue, p.getY());
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_int_int {

      public static class positive_tests {

        @Test
        public void unchanged() {
          Point2 p = new Point2() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("p:" + p);
          final int testValue = 0;
          p.set(testValue, testValue);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Point2 p = new Point2() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("p:" + p);
          final int testValue = 1;
          p.set(testValue, testValue);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2 p = new Point2();
          if (output) System.out.println("p:" + p);
          p.set(null);
          if (output) System.out.println("p:" + p);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Point2 src = new Point2();
          Point2 p = new Point2() {
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
          final int[] calls = {0};
          Point2 src = new Point2(1, 1);
          Point2 p = new Point2() {
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
        Point2 p = new Point2();
        p.onChange();
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
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertTrue(p.equals(PRIMES[0], PRIMES[1]));
        }

        @Test
        public void _false() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p:" + p);
          Assert.assertFalse(p.equals(0, PRIMES[1]));
          Assert.assertFalse(p.equals(PRIMES[0], 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class equals_Object {

      public static class positive_tests {

        @Test
        public void reflexive() {
          Point2 x = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertTrue(x.equals(x));
          Assert.assertEquals(x.hashCode(), x.hashCode());
        }

        @Test
        public void symmetric() {
          Point2 x = new Point2(PRIMES[0], PRIMES[1]);
          Point2 y = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          Assert.assertTrue(x.equals(y) && y.equals(x));
          Assert.assertEquals(x.hashCode(), y.hashCode());
        }

        @Test
        public void transitive() {
          Point2 x = new Point2(PRIMES[0], PRIMES[1]);
          Point2 y = new Point2(PRIMES[0], PRIMES[1]);
          Point2 z = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          if (output) System.out.println("z:" + z);
          Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z));
        }

        @Test
        public void nullity() {
          Point2 x = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(null));
        }

        @Test
        public void type() {
          Point2 x = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(new Object()));
        }

        @Test
        public void Point2F() {
          Point2 p1 = new Point2(PRIMES[0], PRIMES[1]);
          Point2F p2 = new Point2F((float) PRIMES[0], (float) PRIMES[1]);
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
        Point2 p = new Point2();
        String toString = p.toString();
        if (output) System.out.println("p:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toPoint2F {

    @RunWith(Enclosed.class)
    public static class toPoint2F_ {

      public static class positive_tests {

        @Test
        public void equality() {
          Point2 p1 = new Point2(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p1:" + p1);
          Point2F p2 = p1.toPoint2F();
          if (output) System.out.println("p2:" + p2);
          Assert.assertNotNull(p2);
          Assert.assertEquals(p1, p2);
          Assert.assertEquals(p1.hashCode(), p2.hashCode());
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class toPoint2F_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          Point2F dst = null;
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.toPoint2F(dst);
          if (output) System.out.println("result:" + result);
        }

      }

      public static class positive_tests {

        @Test
        public void equality() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          Point2F dst = new Point2F();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.toPoint2F(dst);
          if (output) System.out.println("dst:" + dst);
          Assert.assertNotNull(result);
          Assert.assertEquals(p, result);
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class add {

    @RunWith(Enclosed.class)
    public static class add_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2 p = new Point2();
          p.add(null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2 result = p.add(src);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX());
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY());
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2 p = new Point2();
          p.add(null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2 p = new Point2();
          p.add(p, null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2 dst = new Point2(PRIMES[4], PRIMES[5]);
          Point2 result = p.add(src, dst);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX());
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY());
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class subtract {

    @RunWith(Enclosed.class)
    public static class subtract_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2 p = new Point2();
          p.subtract(null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2 result = p.subtract(src);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX());
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY());
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2 p = new Point2();
          p.subtract(null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2 p = new Point2();
          p.subtract(p, null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2 dst = new Point2(PRIMES[4], PRIMES[5]);
          Point2 result = p.subtract(src, dst);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX());
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY());
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class scale {

    @RunWith(Enclosed.class)
    public static class scale_Point2 {

      public static class positive_tests {

        @Test
        public void scale() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]);
          final double scalar = Math.PI;
          Point2 result = p.scale(scalar);
          Assert.assertEquals((int) (PRIMES[0] * scalar), result.getX());
          Assert.assertEquals((int) (PRIMES[1] * scalar), result.getY());
          Assert.assertSame(p, result);
        }

        @Test
        public void unchanged() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          final double scalar = 1.0;
          Point2 result = p.scale(scalar);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class scale_Point2_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2 p = new Point2();
          final double scalar = Math.PI;
          p.scale(scalar, null);
        }

      }

      public static class positive_tests {

        @Test
        public void scale() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          final double scalar = Math.PI;
          Point2 dst = new Point2(PRIMES[4], PRIMES[5]);
          Point2 result = p.scale(scalar, dst);
          Assert.assertEquals((int) (PRIMES[0] * scalar), result.getX());
          Assert.assertEquals((int) (PRIMES[1] * scalar), result.getY());
          Assert.assertSame(dst, result);
        }

        @Test
        public void unchanged() {
          Point2 p = new Point2(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2 dst = new Point2(PRIMES[2], PRIMES[3]);
          final double scalar = 1.0;
          Point2 result = p.scale(scalar, dst);
          Assert.assertSame(dst, result);
          Assert.assertEquals(p, dst);
        }

      }

    }

  }

}
