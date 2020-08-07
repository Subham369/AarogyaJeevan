package com.example.aarogyajeevan;

public class Methods {

    public void setColorTheme(){

        switch (ConstantColor.color){
            case 0xffF44336:
                ConstantColor.theme = R.style.AppTheme_red;
                break;
            case 0xffE91E63:
                ConstantColor.theme = R.style.AppTheme_pink;
                break;
            case 0xff9C27B0:
                ConstantColor.theme = R.style.AppTheme_darpink;
                break;
            case 0xff673AB7:
                ConstantColor.theme = R.style.AppTheme_violet;
                break;
            case 0xff3F51B5:
                ConstantColor.theme = R.style.AppTheme_blue;
                break;
            case 0xff03A9F4:
                ConstantColor.theme = R.style.AppTheme_skyblue;
                break;
            case 0xff4CAF50:
                ConstantColor.theme = R.style.AppTheme_green;
                break;
            case 0xffFF9800:
                ConstantColor.theme = R.style.AppTheme;
                break;
            case 0xff9E9E9E:
                ConstantColor.theme = R.style.AppTheme_grey;
                break;
            case 0xff795548:
                ConstantColor.theme = R.style.AppTheme_brown;
                break;
            default:
                ConstantColor.theme = R.style.AppTheme;
                break;
        }
    }
}