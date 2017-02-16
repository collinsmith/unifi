package deprecated.unifi2.util;

import com.google.common.base.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import deprecated.unifi2.util.SimplePoolTests.PoolableInteger;

import static unifi.Data.PRIMES;

@RunWith(Enclosed.class)
public class SynchronizedPoolTests {

  private static final boolean output = true;

  @RunWith(Enclosed.class)
  public static class FactoryMethods {

    @RunWith(Enclosed.class)
    public static class synchronizedPool {

      public static class negative_tests {

        @Test(expected = IllegalArgumentException.class)
        public void fails_null() {
          Pools.Pool pool = Pools.synchronizedPool(null);
          if (output) System.out.println("pool:" + pool);
        }

      }

      public static class positive_tests {

        @Test
        public void non_null() {
          Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
          pool = Pools.synchronizedPool(pool);
        }

      }

    }

  }

  @RunWith(Enclosed.class)
  public static class acquire {

    public static class positive_tests {

      @Test
      public void acquires_from_populated_pool_without_supplier() {
        Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        pool = Pools.synchronizedPool(pool);
        PoolableInteger instance = new PoolableInteger();
        pool.release(instance);
        if (output) System.out.println("pool:" + pool);
        PoolableInteger pooled = pool.acquire();
        if (output) System.out.println("pool:" + pool);
        Assert.assertSame(instance, pooled);
      }

      @Test
      public void acquires_from_populated_pool_with_supplier() {
         Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
            new Supplier<PoolableInteger>() {
              @Override
              public PoolableInteger get() {
                Assert.fail("supplier should not be used when pool is populated");
                return null;
              }
            }, 1);
        pool = Pools.synchronizedPool(pool);
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
        Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>(
            new Supplier<PoolableInteger>() {
              @Override
              public PoolableInteger get() {
                return expected;
              }
            }, 1);
        pool = Pools.synchronizedPool(pool);
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
        Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        pool = Pools.synchronizedPool(pool);
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
        Pools.Pool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(PRIMES.length);
        pool = Pools.synchronizedPool(pool);
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
        Pools.Pool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(maxSize);
        pool = Pools.synchronizedPool(pool);
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
        Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        pool = Pools.synchronizedPool(pool);
        String toString = pool.toString();
        if (output) System.out.println("pool:" + toString);
        Assert.assertNotNull(toString);
        Assert.assertFalse(toString.isEmpty());
      }

      @Test
      public void single_pooled_element_pool() {
        Pools.Pool<PoolableInteger> pool = new Pools.SimplePool<PoolableInteger>();
        pool = Pools.synchronizedPool(pool);
        pool.release(new PoolableInteger(PRIMES[0]));
        if (output) System.out.println("pool:" + pool);
      }

      @Test
      public void multiple_pooled_elements_pool() {
        Pools.Pool<PoolableInteger> pool
            = new Pools.SimplePool<PoolableInteger>(PRIMES.length);
        pool = Pools.synchronizedPool(pool);
        for (int prime : PRIMES) {
          pool.release(new PoolableInteger(prime));
        }

        if (output) System.out.println("pool:" + pool);
      }

    }

  }

}
