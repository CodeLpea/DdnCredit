package com.example.lp.ddncredit.mainview.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情接口
 * lp
 * 2019/05/30
 */
public interface ExpressionInterface {
    String expressionChoose = "expressionChoose";//表情选择标志

    String NoPersionExpression = "expression/NoPersionExpression.gif";//没人时的表情
    String CreditExpressioneyeRght = "expression/CreditExpressioneyeRght.gif";//刷卡时候的表情
    String CreditExpressioneyeLeft = "expression/CreditExpressioneyeLeft.gif";//刷卡时候的表情
    String SmiletExpression = "expression/SmiletExpression.gif";//刷卡时候的表情
    String Attract = "expression/attract.gif";//刷卡时候的表情
    String BlinkAlollipop = "expression/BlinkAlollipop.gif";//刷卡时候的表情
    String Eyebrowsguide = "expression/Eyebrowsguide.gif";//刷卡时候的表情
    String Sunglasses = "expression/sunglasses.gif";//刷卡时候的表情
    String Tongue = "expression/tongue.gif";//刷卡时候的表情
    String Wave = "expression/wave.gif";//刷卡时候的表情
    String WearingAmask = "expression/WearingAmask.gif";//刷卡时候的表情

    String[] ExpressionList = {
            SmiletExpression
            , CreditExpressioneyeRght
            , CreditExpressioneyeLeft
            , Attract
            , BlinkAlollipop
            , Eyebrowsguide
            , Sunglasses
            , Tongue
            , Wave
            , WearingAmask
            , NoPersionExpression
            , NoPersionExpression
    };
}
