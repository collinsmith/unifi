package unifi.graphics.old;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import unifi.graphics.old.Point2;
import unifi.graphics.old.Point2F;

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

  @RunWith(Enclosed.class)
  public static class toPoint2 {

    @RunWith(Enclosed.class)
    public static class toPoint2_ {

      public static class positive_tests {

        @Test
        public void equality() {
          Point2F p1 = new Point2F(PRIMES[0], PRIMES[1]);
          if (output) System.out.println("p1:" + p1);
          Point2 p2 = p1.toPoint2();
          if (output) System.out.println("p2:" + p2);
          Assert.assertNotNull(p2);
          Assert.assertEquals((int) p1.getX(), p2.getX());
          Assert.assertEquals((int) p1.getY(), p2.getY());
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class toPoint2_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2 dst = null;
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          Point2 result = p.toPoint2(dst);
          if (output) System.out.println("result:" + result);
        }

      }

      public static class positive_tests {

        @Test
        public void equality() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2 dst = new Point2();
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("dst:" + dst);
          Point2 result = p.toPoint2(dst);
          if (output) System.out.println("result:" + dst);
          Assert.assertNotNull(result);
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class add {

    @RunWith(Enclosed.class)
    public static class add_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F();
          p.add((Point2F) null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2F src = new Point2F(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.add(src);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2F_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2F p = new Point2F();
          p.add((Point2F) null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2F p = new Point2F();
          p.add(p, null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2F src = new Point2F(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2F dst = new Point2F(PRIMES[4], PRIMES[5]);
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.add(src, dst);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(dst, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F();
          p.add((Point2) null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.add(src);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class add_Point2_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2F p = new Point2F();
          p.add((Point2) null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2F p = new Point2F();
          Point2 src = new Point2();
          p.add(src, null);
        }

      }

      public static class positive_tests {

        @Test
        public void add() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
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
          Point2F dst = new Point2F(PRIMES[4], PRIMES[5]);
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.add(src, dst);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] + PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] + PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class subtract {

    @RunWith(Enclosed.class)
    public static class subtract_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F();
          p.subtract((Point2F) null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2F src = new Point2F(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.subtract(src);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2F_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2F p = new Point2F();
          p.subtract((Point2F) null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2F p = new Point2F();
          p.subtract(p, null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2F src = new Point2F(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          Point2F dst = new Point2F(PRIMES[4], PRIMES[5]);
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.subtract(src, dst);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(dst, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2 {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Point2F p = new Point2F();
          p.subtract((Point2) null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          Point2 src = new Point2(PRIMES[2], PRIMES[3]) {
            @Override
            protected void onChange() {
              Assert.fail("src should not be changing");
            }
          };
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          Point2F result = p.subtract(src);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class subtract_Point2_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_src() {
          Point2F p = new Point2F();
          p.subtract((Point2) null, p);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2F p = new Point2F();
          Point2 src = new Point2();
          p.subtract(src, null);
        }

      }

      public static class positive_tests {

        @Test
        public void subtract() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
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
          Point2F dst = new Point2F(PRIMES[4], PRIMES[5]);
          if (output) System.out.println("p:" + p);
          if (output) System.out.println("src:" + src);
          if (output) System.out.println("dst:" + dst);
          Point2F result = p.subtract(src, dst);
          if (output) System.out.println("result:" + result);
          Assert.assertEquals(PRIMES[0] - PRIMES[2], result.getX(), DELTA);
          Assert.assertEquals(PRIMES[1] - PRIMES[3], result.getY(), DELTA);
          Assert.assertSame(dst, result);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class scale {

    @RunWith(Enclosed.class)
    public static class scale_double {

      public static class positive_tests {

        @Test
        public void scale() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]);
          final double scalar = Math.PI;
          Point2F result = p.scale(scalar);
          Assert.assertEquals((int) (PRIMES[0] * scalar), result.getX(), DELTA);
          Assert.assertEquals((int) (PRIMES[1] * scalar), result.getY(), DELTA);
          Assert.assertSame(p, result);
        }

        @Test
        public void unchanged() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          final double scalar = 1.0;
          Point2F result = p.scale(scalar);
          Assert.assertSame(p, result);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class scale_double_Point2F {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_dst() {
          Point2F p = new Point2F();
          final double scalar = Math.PI;
          p.scale(scalar, null);
        }

      }

      public static class positive_tests {

        @Test
        public void scale() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          final double scalar = Math.PI;
          Point2F dst = new Point2F(PRIMES[4], PRIMES[5]);
          Point2F result = p.scale(scalar, dst);
          Assert.assertEquals((int) (PRIMES[0] * scalar), result.getX(), DELTA);
          Assert.assertEquals((int) (PRIMES[1] * scalar), result.getY(), DELTA);
          Assert.assertSame(dst, result);
        }

        @Test
        public void unchanged() {
          Point2F p = new Point2F(PRIMES[0], PRIMES[1]) {
            @Override
            protected void onChange() {
              Assert.fail("p should not be changing");
            }
          };
          Point2F dst = new Point2F(PRIMES[2], PRIMES[3]);
          final double scalar = 1.0;
          Point2F result = p.scale(scalar, dst);
          Assert.assertSame(dst, result);
          Assert.assertEquals(p, dst);
        }

      }

    }

  }

}
