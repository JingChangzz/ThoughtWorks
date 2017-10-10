package com.thoughttworks.badmintonbooking.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ZhangJing on 2017/9/15.
 */
public class InputValueCheckTest {
    InputValueCheck inputValueCheck = new InputValueCheck();

    @Test
    public void isBookingInputValid() throws Exception {
        String[] in1 = {"U002", "2017-09-12", "19:00~22:00", "A"};
        Assert.assertEquals(false, inputValueCheck.isBookingInputValid(in1));
    }

    @Test
    public void isCanclingInputValid() throws Exception {
        String[] in1 = {"U002", "2017-09-12", "19:00~22:00", "A", "C"};
        Assert.assertEquals(true, inputValueCheck.isCanclingInputValid(in1));
    }

}