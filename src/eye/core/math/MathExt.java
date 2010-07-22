/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.math;

/**
 *
 * @author spr1ng
 * @version $Id$
 */
public class MathExt {

    /**
     *
     * @param dbl округляемое число
     * @param rounding кол-во знаков после запятой
     * @return
     */
    public static double round(double dbl, int rounding){
        double hackCoeff = Math.pow(10, rounding);
        return (double)Math.round((dbl * hackCoeff)) / hackCoeff;
    }

}
