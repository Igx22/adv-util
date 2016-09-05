package adv.util.patterns;

import org.junit.Assert;

public abstract class BidObj {
    protected Double bidPrice;

    protected BidObj() {
    }

    protected BidObj(BidObj o) {
        this.bidPrice = o.getBidPrice();
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public abstract Factory factory();

    public abstract Value value();

    @Override
    public String toString() {
        return "BidObj{" +
                "bidPrice=" + bidPrice +
                '}';
    }

    public static class Value extends BidObj {

        public Value(Factory factory) {
            super(factory);
        }

        @Override
        public Factory factory() {
            return new Factory(this);
        }

        @Override
        public Value value() {
            return this;
        }
    }

    public static class Factory extends BidObj {
        protected Double bidPrice;

        public Factory() {
            super();
        }

        public Factory(Value value) {

        }

        @Override
        public Double getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(Double bidPrice) {
            this.bidPrice = bidPrice;
        }

        @Override
        public Factory factory() {
            return this;
        }

        @Override
        public Value value() {
            return new Value(this);
        }
    }


    public static void main(String[] args) {
        BidObj.Factory factory1 = new BidObj.Factory();
        factory1.setBidPrice(200.0);

        BidObj.Value value1 = factory1.value();
        new Thread() {

            @Override
            public void run() {
                System.out.printf("value1 is %s\n", value1);
                BidObj.Factory factory2 = value1.factory();
                factory2.setBidPrice(300.0);
                BidObj.Value value2 = factory2.value();
                System.out.printf("factory2 is %s\n", factory2);
                Assert.assertEquals(factory1.getBidPrice(), 200.0, 0.0001);
                Assert.assertEquals(value1.getBidPrice(), 200.0, 0.0001);
                Assert.assertEquals(factory2.getBidPrice(), 300.0, 0.0001);
                Assert.assertEquals(value2.getBidPrice(), 300.0, 0.0001);
            }
        }.start();
    }
}
