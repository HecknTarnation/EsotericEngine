package com.heckntarnation.rpgengine.heckscript.cons;

import com.heckntarnation.rpgengine.heckscript.exception.HeckRuntimeException;

/**
 *
 * @author Ben
 */
public class HeckNumber {

    public String value;

    public Position start;
    public Position end;
    public Context context;

    public Exception error;

    public HeckNumber(String value) {
        this.value = value;
    }

    public HeckNumber(int value) {
        this.value = value + "";
    }

    public HeckNumber(float value) {
        this.value = value + "";
    }

    public HeckNumber setContext(Context context) {
        this.context = context;
        return this;
    }

    public HeckNumber setPosition(Position start, Position end) {
        this.start = start;
        this.end = end;
        return this;
    }

    public float getValue() {
        return Float.parseFloat(this.value);
    }

    public HeckNumber addedTo(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() + oth.getValue()).setContext(this.context);
        }
        return null;
    }

    public HeckNumber subtractBy(Object other) {
        if (other instanceof HeckNumber) {
            if (other instanceof HeckNumber) {
                HeckNumber oth = (HeckNumber) other;
                return new HeckNumber(this.getValue() - oth.getValue()).setContext(this.context);
            }
        }
        return null;
    }

    public HeckNumber multiplyBy(Object other) {
        if (other instanceof HeckNumber) {
            if (other instanceof HeckNumber) {
                HeckNumber oth = (HeckNumber) other;
                return new HeckNumber(this.getValue() * oth.getValue()).setContext(this.context);
            }
        }
        return null;
    }

    public HeckNumber divideBy(Object other) throws HeckRuntimeException {
        if (other instanceof HeckNumber) {
            if (other instanceof HeckNumber) {
                HeckNumber oth = (HeckNumber) other;
                float a = this.getValue() / oth.getValue();
                if (a == Float.POSITIVE_INFINITY || a == Float.NEGATIVE_INFINITY) {
                    this.error = HeckRuntimeException.contextRuntimeException("Divide by zero.", this.context, this.start);
                    return this;
                }
                return new HeckNumber(a).setContext(this.context);
            }
        }
        return null;
    }

    public HeckNumber powerBy(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber((float) Math.pow(this.getValue(), oth.getValue())).setContext(this.context);
        }
        return null;
    }

    public HeckNumber compareEQ(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() == oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber compareNE(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() != oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber compareLT(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() < oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber compareGT(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() > oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber compareLTE(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() <= oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber compareGTE(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() >= oth.getValue() ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber andBy(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() == 1 && oth.getValue() == 1 ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber orBy(Object other) {
        if (other instanceof HeckNumber) {
            HeckNumber oth = (HeckNumber) other;
            return new HeckNumber(this.getValue() == 1 || oth.getValue() == 1 ? 1 : 0).setContext(context);
        }
        return null;
    }

    public HeckNumber not() {
        return new HeckNumber((this.getValue() == 0 ? 1 : 0));
    }

    public boolean isTrue() {
        return Float.parseFloat(this.value) != 0;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
