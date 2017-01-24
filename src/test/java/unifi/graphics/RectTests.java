package unifi.graphics;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class RectTests {

  private interface ChangeListener {
    void onChange();
  }

  private static final boolean output = true;

  private static Rect createRect() {
    return createRect(null);
  }

  private static Rect createRect(ChangeListener changeListener) {
    return createRect(0, 0, 0, 0, changeListener);
  }

  private static Rect createRect(int left, int top, int right, int bottom) {
    return createRect(left, top, right, bottom, null);
  }

  private static Rect createRect(int left, int top, int right, int bottom,
                                 ChangeListener changeListener) {
    final ChangeListener l = changeListener;
    Rect r = new Rect() {
      @Override
      protected void onChange() {
        super.onChange();
        if (l != null) {
          l.onChange();
        }
      }
    };

    try {
      Field f;
      f = Rect.class.getDeclaredField("left");
      f.setAccessible(true);
      f.setInt(r, left);
      f = Rect.class.getDeclaredField("top");
      f.setAccessible(true);
      f.setInt(r, top);
      f = Rect.class.getDeclaredField("right");
      f.setAccessible(true);
      f.setInt(r, right);
      f = Rect.class.getDeclaredField("bottom");
      f.setAccessible(true);
      f.setInt(r, bottom);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return r;
    }
  }

  private static boolean equals(Rect r, int left, int top, int right, int bottom) {
    assert r != null;
    try {
      Field f;
      f = Rect.class.getDeclaredField("left");
      f.setAccessible(true);
      if (f.getInt(r) != left) {
        return false;
      }

      f = Rect.class.getDeclaredField("top");
      f.setAccessible(true);
      if (f.getInt(r) != top) {
        return false;
      }

      f = Rect.class.getDeclaredField("right");
      f.setAccessible(true);
      if (f.getInt(r) != right) {
        return false;
      }

      f = Rect.class.getDeclaredField("bottom");
      f.setAccessible(true);
      if (f.getInt(r) != bottom) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

  private static boolean equals(Rect r1, Rect r2) {
    assert r1 != null;
    assert r2 != null;
    try {
      Field f;
      f = Rect.class.getDeclaredField("left");
      f.setAccessible(true);
      if (f.getInt(r1) != f.getInt(r2)) {
        return false;
      }

      f = Rect.class.getDeclaredField("top");
      f.setAccessible(true);
      if (f.getInt(r1) != f.getInt(r2)) {
        return false;
      }

      f = Rect.class.getDeclaredField("right");
      f.setAccessible(true);
      if (f.getInt(r1) != f.getInt(r2)) {
        return false;
      }

      f = Rect.class.getDeclaredField("bottom");
      f.setAccessible(true);
      if (f.getInt(r1) != f.getInt(r2)) {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

  private static int getSide(Rect r, String name) {
    assert r != null;
    try {
      Field f;
      f = Rect.class.getDeclaredField(name);
      f.setAccessible(true);
      return f.getInt(r);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }

    return Integer.MIN_VALUE;
  }

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Rect_ {

      public static class positive_tests {

        @Test
        public void empty() {
          Rect r = new Rect();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 0, 0));
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
        public void empty() {
          Rect r = new Rect(0, 0, 0, 0);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 0, 0));
        }

        @Test
        public void non_empty() {
          Rect r = new Rect(0, 0, 1, 1);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 1, 1));
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
        public void non_null() throws NoSuchFieldException {
          Rect src = createRect(PRIMES[0], PRIMES[1], PRIMES[2], PRIMES[3]);
          if (output) System.out.println("src:" + src);
          Rect r = new Rect(src);
          if (output) System.out.println("r:" + src);
          Assert.assertNotSame(src, r);
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
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "left"));
        Assert.assertTrue(RectTests.equals(r, testValue, 0, 0, 0));
      }

      @Test
      public void change() {
        final boolean[] changed = { false };
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setLeft(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "left"));
        Assert.assertTrue(RectTests.equals(r, testValue, 0, 0, 0));
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setTop {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "top"));
        Assert.assertTrue(RectTests.equals(r, 0, testValue, 0, 0));
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setTop(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "top"));
        Assert.assertTrue(RectTests.equals(r, 0, testValue, 0, 0));
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setRight {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "right"));
        Assert.assertTrue(RectTests.equals(r, 0, 0, testValue, 0));
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setRight(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "right"));
        Assert.assertTrue(RectTests.equals(r, 0, 0, testValue, 0));
        Assert.assertTrue(changed[0]);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class setBottom {

    public static class positive_tests {

      @Test
      public void unchanged() {
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            Assert.fail("onChange should only be called when a state change is made");
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 0;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "bottom"));
        Assert.assertTrue(RectTests.equals(r, 0, 0, 0, testValue));
      }

      @Test
      public void change() {
        final boolean[] changed = {false};
        Rect r = createRect(new ChangeListener() {
          @Override
          public void onChange() {
            changed[0] = true;
          }
        });

        if (output) System.out.println("r:" + r);
        final int testValue = 1;
        r.setBottom(testValue);
        if (output) System.out.println("r:" + r);
        Assert.assertEquals(testValue, getSide(r, "bottom"));
        Assert.assertTrue(RectTests.equals(r, 0, 0, 0, testValue));
        Assert.assertTrue(changed[0]);
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
          Rect r = createRect();
          if (output) System.out.println("r:" + r);
          r.set(1, 0, 0, 0);
          if (output) System.out.println("r:" + r);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_top_greater_than_bottom() {
          Rect r = createRect();
          if (output) System.out.println("r:" + r);
          r.set(0, 1, 0, 0);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          });

          if (output) System.out.println("r:" + r);
          final int testValue = 0;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, testValue, testValue, testValue, testValue));
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("r:" + r);
          final int testValue = 1;
          r.set(testValue, testValue, testValue, testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, testValue, testValue, testValue, testValue));
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
          Rect r = createRect();
          if (output) System.out.println("r:" + r);
          r.set(null);
          if (output) System.out.println("r:" + r);
        }

      }

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect src = createRect();
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          });

          if (output) System.out.println("src:" + r);
          if (output) System.out.println("r:" + r);
          r.set(src);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, src));
        }

        @Test
        public void changed() throws NoSuchFieldException {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect src = createRect(0, 0, 1, 1);
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("src:" + r);
          if (output) System.out.println("r:" + r);
          r.set(src);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, src));
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
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          });

          if (output) System.out.println("r:" + r);
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 0, 0));
          Assert.assertTrue(r.isEmpty());
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = createRect(1, 1, 1, 1, new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("r:" + r);
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 0, 0));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          Assert.assertTrue(r.isEmpty());
        }

        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = createRect(0, 0, 1, 1, new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          r.setEmpty();
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, 0, 0, 0, 0));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertNotEquals(initialIsEmpty, isEmpty);
        }
        
      }
      
    }

    @RunWith(Enclosed.class)
    public static class setEmpty_int {

      public static class positive_tests {

        @Test
        public void unchanged() {
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              Assert.fail("onChange should only be called when a state change is made");
            }
          });

          if (output) System.out.println("r:" + r);
          final int testValue = 0;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, testValue, testValue, testValue, testValue));
          Assert.assertTrue(r.isEmpty());
        }

        @Test
        public void change() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = createRect(new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("r:" + r);
          final int testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          Assert.assertTrue(r.isEmpty());
        }
        
        @Test
        public void isEmpty_state_changed() {
          final boolean[] changed = {false};
          final int[] calls = {0};
          Rect r = createRect(0, 0, 1, 1, new ChangeListener() {
            @Override
            public void onChange() {
              changed[0] = true;
              calls[0]++;
            }
          });

          if (output) System.out.println("r:" + r);
          final boolean initialIsEmpty = r.isEmpty();
          final int testValue = 1;
          r.setEmpty(testValue);
          if (output) System.out.println("r:" + r);
          Assert.assertTrue(RectTests.equals(r, testValue, testValue, testValue, testValue));
          Assert.assertTrue(changed[0]);
          Assert.assertEquals(1, calls[0]);
          final boolean isEmpty = r.isEmpty();
          Assert.assertTrue(isEmpty);
          Assert.assertNotEquals(initialIsEmpty, isEmpty);
        }

      }

    }
    
  }

  @RunWith(Enclosed.class)
  public static class toString {

    public static class positive_tests {

      @Test
      public void nullity() {
        Rect r = createRect();
        String toString = r.toString();
        if (output) System.out.println("r:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

    }

  }

}
