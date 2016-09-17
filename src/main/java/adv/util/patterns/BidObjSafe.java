package adv.util.patterns;

import org.junit.Assert;

public interface BidObjSafe {

    Delegate getDelegate();

    BidObjSafe.Factory factory();

    BidObjSafe.Value value();

    // add getters here
    default Double getBidPrice() {
        return getDelegate().getBidPrice();
    }

    public static class Factory implements BidObjSafe {
        private Delegate delegate;

        public Factory() {
            this.delegate = new Delegate();
        }

        public Factory(Delegate delegate) {
            this.delegate = delegate;
        }

        @Override
        public Delegate getDelegate() {
            return delegate;
        }

        @Override
        public BidObjSafe.Factory factory() {
            return this;
        }

        @Override
        public BidObjSafe.Value value() {
            return new Value(this.delegate);
        }

        @Override
        public String toString() {
            return "Factory{" +
                    "delegate=" + delegate +
                    '}';
        }

        // add setters here
        public void setBidPrice(Double bidPrice) {
            this.delegate.setBidPrice(bidPrice);
        }

    }

    public static class Value implements BidObjSafe {
        private final Delegate delegate;

        public Value(Delegate delegate) {
            this.delegate = delegate;
        }

        @Override
        public Delegate getDelegate() {
            return delegate;
        }

        @Override
        public Factory factory() {
            return new Factory((Delegate) delegate.clone());
        }

        @Override
        public Value value() {
            return this;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "delegate=" + delegate +
                    '}';
        }
    }

    public static class Delegate implements Cloneable {
        private Double bidPrice;

        public Delegate() {
        }

        public Double getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(Double bidPrice) {
            this.bidPrice = bidPrice;
        }

        @Override
        public String toString() {
            return "Delegate{" +
                    "bidPrice=" + bidPrice +
                    '}';
        }

        // add deep clone here
        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void main(String[] args) {
        BidObjSafe.Factory factory1 = new BidObjSafe.Factory();
        factory1.setBidPrice(200.0);

        BidObjSafe.Value value1 = factory1.value();
        new Thread() {

            @Override
            public void run() {
                System.out.printf("value1 is %s\n", value1);
                BidObjSafe.Factory factory2 = value1.factory();
                factory2.setBidPrice(300.0);
                BidObjSafe.Value value2 = factory2.value();
                System.out.printf("factory2 is %s\n", factory2);
                Assert.assertEquals(factory1.getBidPrice(), 200.0, 0.0001);
                Assert.assertEquals(value1.getBidPrice(), 200.0, 0.0001);
                Assert.assertEquals(factory2.getBidPrice(), 300.0, 0.0001);
                Assert.assertEquals(value2.getBidPrice(), 300.0, 0.0001);
            }
        }.start();
    }
}
