package unifi.util;

import com.google.common.base.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class SimplePoolTests {

  private static final boolean output = true;

  static class PoolableInteger implements Pools.Poolable {

    private Integer i;

    public PoolableInteger() {
      this(0);
    }

    public PoolableInteger(int i) {
      this.i = i;
    }

    @Override
    public void recycle() {
      this.i = null;
    }

    @Override
    public String toString() {
      return i.toString();
    }
  }

  @RunWith(Enclosed.class)
  public static class Constructors {

    @RunWith(Enclosed.class)
    public static class Pools_ {

      public static class positive_tests {

        @Test
        public void non_instantiability()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
          Constructor<Pools> constructor = Pools.class.getDeclaredConstructor();
          Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
          constructor.setAccessible(true);
          constructor.newInstance();
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class SimplePool_ {

      public static class positive_tests {

        @Test
        public void constructs() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
          if (output) System.out.println("pool:" + pool);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class SimplePool_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_maxSize() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(0);
          if (output) System.out.println("pool:" + pool);
        }

      }

      public static class positive_tests {

        @Test
        public void constructs() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(1);
          if (output) System.out.println("pool:" + pool);
        }

      }

    }

    @RunWith(Enclosed.class)
    public static class SimplePool_Supplier_int {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_invalid_maxSize() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
              new Supplier<PoolableInteger>() {
                @Override
                public PoolableInteger get() {
                  return null;
                }
              }, 0);
          if (output) System.out.println("pool:" + pool);
        }

        @Test(expected = IllegalArgumentException.class)
        public void fails_null_supplier() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
              null, 1);
          if (output) System.out.println("pool:" + pool);
        }

      }

      public static class positive_tests {

        @Test
        public void constructs() {
          Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
              new Supplier<PoolableInteger>() {
                @Override
                public PoolableInteger get() {
                  return null;
                }
              }, 1);
          if (output) System.out.println("pool:" + pool);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class acquire {

    public static class positive_tests {

      @Test
      public void acquires_from_populated_pool_without_supplier() {
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        PoolableInteger instance = new PoolableInteger();
        pool.release(instance);
        if (output) System.out.println("pool:" + pool);
        PoolableInteger pooled = pool.acquire();
        if (output) System.out.println("pool:" + pool);
        Assert.assertSame(instance, pooled);
      }

      @Test
      public void acquires_from_populated_pool_with_supplier() {
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
            new Supplier<PoolableInteger>() {
              @Override
              public PoolableInteger get() {
                Assert.fail("supplier should not be used when pool is populated");
                return null;
              }
            }, 1);
        PoolableInteger instance = new PoolableInteger();
        pool.release(instance);
        if (output) System.out.println("pool:" + pool);
        PoolableInteger pooled = pool.acquire();
        if (output) System.out.println("pool:" + pool);
        Assert.assertSame(instance, pooled);
      }

      @Test
      public void acquires_from_supplier() {
        final PoolableInteger expected = new PoolableInteger();
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
            new Supplier<PoolableInteger>() {
              @Override
              public PoolableInteger get() {
                return expected;
              }
            }, 1);
        if (output) System.out.println("pool:" + pool);
        PoolableInteger pooled = pool.acquire();
        if (output) System.out.println("pool:" + pool);
        Assert.assertSame(expected, pooled);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class release {

    public static class negative_tests {

      @Test(expected = IllegalStateException.class)
      public void fails_already_released() {
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        PoolableInteger instance = new PoolableInteger();
        pool.release(instance);
        if (output) System.out.println("pool:" + pool);
        pool.release(instance);
        if (output) System.out.println("pool:" + pool);
      }

    }

    public static class positive_tests {

      @Test
      public void true_() {
        Pools.SimplePool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(PRIMES.length);
        for (int prime : PRIMES) {
          boolean released = pool.release(new PoolableInteger(prime));
          if (output) System.out.println("pool:" + pool);
          Assert.assertTrue(released);
        }

        if (output) System.out.println("pool:" + pool);
      }

      @Test
      public void false_() {
        final int maxSize = 3;
        Pools.SimplePool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(maxSize);
        for (int i = 0; i < maxSize; i++) {
          pool.release(new PoolableInteger(PRIMES[i]));
        }

        if (output) System.out.println("pool:" + pool);
        for (int i = maxSize; i < PRIMES.length; i++) {
          int prime = PRIMES[i];
          boolean released = pool.release(new PoolableInteger(prime));
          if (output) System.out.println("pool:" + pool);
          Assert.assertFalse(released);
        }

        if (output) System.out.println("pool:" + pool);
      }

    }

  }

  @RunWith(Enclosed.class)
  public static class toString {

    public static class positive_tests {

      @Test
      public void non_null_non_empty_string() {
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        String toString = pool.toString();
        if (output) System.out.println("pool:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

      @Test
      public void single_pooled_element_pool() {
        Pools.SimplePool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        pool.release(new PoolableInteger(PRIMES[0]));
        if (output) System.out.println("pool:" + pool);
      }

      @Test
      public void multiple_pooled_elements_pool() {
        Pools.SimplePool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(PRIMES.length);
        for (int prime : PRIMES) {
          pool.release(new PoolableInteger(prime));
        }

        if (output) System.out.println("pool:" + pool);
      }

    }

  }

}
