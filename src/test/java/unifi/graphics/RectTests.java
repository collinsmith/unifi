package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class RectTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Rect_ {

      public static class positive_tests {

        @Test
        public void empty() {
          Rect r = new Rect();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Rect_int_int_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_left_greater_than_right() {
          Rect r = new Rect(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          Rect r = new Rect(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_empty() {
          Rect r = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class Rect_Rect {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Rect r = new Rect(null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Rect src = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("src:" + src);
          Rect r = new Rect(src);
          if (output) System.out.println("r:" + r);
          Assert.assertEquals(src, r);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setLeft {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = new Rect() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getLeft());
      }

      @Test
      public void change() {
        final boolean[] changed = { false };
        Rect r = new Rect(0, 0, 1, 0) {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getLeft());
        Assert.assertTrue(changed[0]);
      }

      @Test
      public void right_reassignment() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getRight());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setTop {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = new Rect() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getTop());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = new Rect(0, 0, 0, 1) {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getTop());
        Assert.assertTrue(changed[0]);
      }

      @Test
      public void bottom_reassignment() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getBottom());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setRight {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = new Rect() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getRight());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getRight());
        Assert.assertTrue(changed[0]);
      }

      @Test
      public void left_reassignment() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = -1;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getLeft());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setBottom {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = new Rect() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getBottom());
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getBottom());
        Assert.assertTrue(changed[0]);
      }

      @Test
      public void top_reassignment() {
        final boolean[] changed = {false};
        Rect r = new Rect() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final int testValue = -1;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getTop());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_int_int_int_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_left_greater_than_right() {
          Rect r = new Rect();
          if (output) System.out.println("r:" + r);
          r.set(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          Rect r = new Rect();
          if (output) System.out.println("r:" + r);
          r.set(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect r = new Rect() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("r:" + r);
          final int testValue = 0;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = new Rect() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final int testValue = 1;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_Rect {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Rect r = new Rect();
          if (output) System.out.println("r:" + r);
          r.set(null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect src = new Rect();
          Rect r = new Rect() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("src:" + r);
          if (output) System.out.println("r:" + r);
          r.set(src);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(src));
        }

        @Test
        public void changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect src = new Rect(0, 0, 1, 1);
          Rect r = new Rect() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("src:" + r);
          if (output) System.out.println("r:" + r);
          r.set(src);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(src));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

  }
  
  @RunWith(Enclosed.class)
  public static class setEmpty {
    
    @RunWith(Enclosed.class)
    public static class setEmpty_ {
      
      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect r = new Rect() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("r:" + r);
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = new Rect(1, 1, 1, 1) {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
        }

        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = new Rect(0, 0, 1, 1) {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          Assert.assertEquals(1, r.getWidth());
          Assert.assertEquals(1, r.getHeight());
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
          Assert.assertNotEquals(initialIsEmpty, isEmpty);
        }
        
      }
      
    }

    @RunWith(Enclosed.class)
    public static class setEmpty_int {

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect r = new Rect() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("r:" + r);
          final int testValue = 0;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = new Rect() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final int testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
        }
        
        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = new Rect(0, 0, 1, 1) {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          Assert.assertEquals(1, r.getWidth());
          Assert.assertEquals(1, r.getHeight());
          final int testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertEquals(0, r.getWidth());
          Assert.assertEquals(0, r.getHeight());
          Assert.assertNotEquals(initialIsEmpty, isEmpty);
        }

      }

    }
    
  }

  @RunWith(Enclosed.class)
  public static class onChange {

    public static class positive_tests {

      @Test
      public void onChange() {
        Rect r = new Rect();
        r.onChange();
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class equals {

    @RunWith(Enclosed.class)
    public static class equals_int_int_int_int {

      public static class positive_tests {

        @Test
        public void _true() {
          Rect r = new Rect(0, 0, 0, 0);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
        }

        @Test
        public void _false() {
          Rect r = new Rect(0, 0, 0, 0);
          if (output) System.out.println("r:" + r);
          Assert.assertFalse(r.equals(1, 0, 0, 0));
          Assert.assertFalse(r.equals(0, 1, 0, 0));
          Assert.assertFalse(r.equals(0, 0, 1, 0));
          Assert.assertFalse(r.equals(0, 0, 0, 1));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class equals_Object {

      public static class positive_tests {

        @Test
        public void reflexive() {
          Rect x = new Rect(0, 0, 0, 0);
          if (output) System.out.println("x:" + x);
          Assert.assertTrue(x.equals(x));
          Assert.assertEquals(x.hashCode(), x.hashCode());
        }

        @Test
        public void symmetric() {
          Rect x = new Rect(0, 0, 0, 0);
          Rect y = new Rect(0, 0, 0, 0);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          Assert.assertTrue(x.equals(y) && y.equals(x));
          Assert.assertEquals(x.hashCode(), y.hashCode());
        }

        @Test
        public void transitive() {
          Rect x = new Rect(0, 0, 0, 0);
          Rect y = new Rect(0, 0, 0, 0);
          Rect z = new Rect(0, 0, 0, 0);
          if (output) System.out.println("x:" + x);
          if (output) System.out.println("y:" + y);
          if (output) System.out.println("z:" + z);
          Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z));
        }

        @Test
        public void nullity() {
          Rect x = new Rect(0, 0, 0, 0);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(null));
        }

        @Test
        public void type() {
          Rect x = new Rect(0, 0, 0, 0);
          if (output) System.out.println("x:" + x);
          Assert.assertFalse(x.equals(new Object()));
        }

        @Test
        public void RectF() {
          Rect r1 = new Rect(1, 1, 1, 1);
          RectF r2 = new RectF(1.0f, 1.0f, 1.0f, 1.0f);
          if (output) System.out.println("r1:" + r1);
          if (output) System.out.println("r2:" + r2);
          Assert.assertEquals(r1, r2);
          Assert.assertEquals(r1.hashCode(), r2.hashCode());
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toString {

    public static class positive_tests {

      @Test
      public void nullity() {
        Rect r = new Rect();
        String toString = r.toString();
        if (output) System.out.println("r:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toRectF {

    @RunWith(Enclosed.class)
    public static class toRectF_ {

      public static class positive_tests {

        @Test
        public void equality() {
          Rect r1 = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("r1:" + r1);
          RectF r2 = r1.toRectF();
          if (output) System.out.println("r2:" + r2);
          Assert.assertNotNull(r2);
          Assert.assertEquals(r1, r2);
          Assert.assertEquals(r1.hashCode(), r2.hashCode());
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class toRectF_RectF {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Rect r = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          RectF dst = null;
          if (output) System.out.println("r:" + r);
          if (output) System.out.println("dst:" + dst);
          RectF result = r.toRectF(dst);
          if (output) System.out.println("result:" + result);
        }

      }

      public static class positive_tests {

        @Test
        public void equality() {
          Rect r = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          RectF dst = new RectF();
          if (output) System.out.println("r:" + r);
          if (output) System.out.println("dst:" + dst);
          RectF result = r.toRectF(dst);
          if (output) System.out.println("dst:" + dst);
          Assert.assertNotNull(result);
          Assert.assertEquals(r, result);
          Assert.assertSame(dst, result);
        }

      }

    }

  }

}
