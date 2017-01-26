package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class RectFTests {

  private static final boolean output = true;

  private static final double DELTA = 1e-15;

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class RectF_ {

      public static class positive_tests {

        @Test
        public void empty() {
          RectF r = new RectF();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class RectF_float_float_float_float {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_left_greater_than_right() {
          RectF r = new RectF(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          RectF r = new RectF(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_empty() {
          RectF r = new RectF(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]));
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class RectF_RectF {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          RectF r = new RectF((RectF) null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          RectF src = new RectF(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("src:" + src);
          RectF r = new RectF(src);
          if (output) System.out.println("r:" + src);
          Assert.assertEquals(src, r);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class RectF_Rect {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          RectF r = new RectF((Rect) null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Rect src = new Rect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("src:" + src);
          RectF r = new RectF(src);
          if (output) System.out.println("r:" + src);
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
        RectF r = new RectF() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 0;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getLeft(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = { false };
        RectF r = new RectF() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 1;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getLeft(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setTop {

    public static class positive_tests {

      @Test
      public void unchanged() {
        RectF r = new RectF() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 0;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getTop(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        RectF r = new RectF() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 1;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getTop(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setRight {

    public static class positive_tests {

      @Test
      public void unchanged() {
        RectF r = new RectF() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 0;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getRight(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        RectF r = new RectF() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 1;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getRight(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setBottom {

    public static class positive_tests {

      @Test
      public void unchanged() {
        RectF r = new RectF() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 0;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getBottom(), DELTA);
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        RectF r = new RectF() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        };

        if (output) System.out.println("r:" + r);
        final float testValue = 1;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, r.getBottom(), DELTA);
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class set {

    @RunWith(Enclosed.class)
    public static class set_float_float_float_float {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_left_greater_than_right() {
          RectF r = new RectF();
          if (output) System.out.println("r:" + r);
          r.set(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          RectF r = new RectF();
          if (output) System.out.println("r:" + r);
          r.set(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          RectF r = new RectF() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("r:" + r);
          final float testValue = 0;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          RectF r = new RectF() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final float testValue = 1;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class set_RectF {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          RectF r = new RectF();
          if (output) System.out.println("r:" + r);
          r.set((RectF) null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          RectF src = new RectF();
          RectF r = new RectF() {
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
          RectF src = new RectF(0, 0, 1, 1);
          RectF r = new RectF() {
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

    @RunWith(Enclosed.class)
    public static class set_Rect {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          RectF r = new RectF();
          if (output) System.out.println("r:" + r);
          r.set((Rect) null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect src = new Rect();
          RectF r = new RectF() {
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
          RectF r = new RectF() {
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
          RectF r = new RectF() {
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
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          RectF r = new RectF(1, 1, 1, 1) {
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
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
        }

        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          RectF r = new RectF(0, 0, 1, 1) {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          Assert.assertEquals(1, r.getWidth(), DELTA);
          Assert.assertEquals(1, r.getHeight(), DELTA);
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
          Assert.assertNotEquals(initialIsEmpty, isEmpty);
        }
        
      }
      
    }

    @RunWith(Enclosed.class)
    public static class setEmpty_float {

      public static class positive_tests {

        @Test
        public void unchanged() {
          RectF r = new RectF() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          };

          if (output) System.out.println("r:" + r);
          final float testValue = 0;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          RectF r = new RectF() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final float testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          Assert.assertTrue(r.isEmpty());
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
        }
        
        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          RectF r = new RectF(0, 0, 1, 1) {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          };

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          Assert.assertEquals(1, r.getWidth(), DELTA);
          Assert.assertEquals(1, r.getHeight(), DELTA);
          final float testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertEquals(0, r.getWidth(), DELTA);
          Assert.assertEquals(0, r.getHeight(), DELTA);
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
        RectF r = new RectF();
        r.onChange();
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class equals {

    @RunWith(Enclosed.class)
    public static class equals_float_float_float_float {

      public static class positive_tests {

        @Test
        public void _true() {
          RectF r = new RectF(0, 0, 0, 0);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(r.equals(0, 0, 0, 0));
        }

        @Test
        public void _false() {
          RectF r = new RectF(0, 0, 0, 0);
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
          RectF x = new RectF(1.1f, 1.2f, 1.3f, 1.4f);
          Assert.assertTrue(x.equals(x));
          Assert.assertEquals(x.hashCode(), x.hashCode());
        }

        @Test
        public void symmetric() {
          RectF x = new RectF(0, 0, 0, 0);
          RectF y = new RectF(0, 0, 0, 0);
          Assert.assertTrue(x.equals(y) && y.equals(x));
          Assert.assertEquals(x.hashCode(), y.hashCode());
        }

        @Test
        public void transitive() {
          RectF x = new RectF(0, 0, 0, 0);
          RectF y = new RectF(0, 0, 0, 0);
          RectF z = new RectF(0, 0, 0, 0);
          Assert.assertTrue(x.equals(y) && y.equals(z) && x.equals(z));
        }

        @Test
        public void nullity() {
          RectF x = new RectF(0, 0, 0, 0);
          Assert.assertFalse(x.equals(null));
        }

        @Test
        public void type() {
          RectF x = new RectF(0, 0, 0, 0);
          Assert.assertFalse(x.equals(new Object()));
        }

        @Test
        public void Rect() {
          Rect r1 = new Rect(1, 1, 1, 1);
          RectF r2 = new RectF(1.0f, 1.0f, 1.0f, 1.0f);
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
        RectF r = new RectF();
        String toString = r.toString();
        if (output) System.out.println("r:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

}
