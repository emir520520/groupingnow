package ca.sheridancollege.fangyux.Utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultEntity<T> {

    private static String success="SUCCESS";

    private static String fail="FAIL";

    //用来封装当前请求结果
    private String result;

    //请求处理失败时返回的错误信息
    private String msg;

    //返回的数据
    private T data;

    public static <T> ResultEntity<T> successWithoutData(){
        return new ResultEntity<T>(success,null,null);
    }

    public static <T> ResultEntity<T> successWithtData(T data){
        return new ResultEntity<T>(success,null,data);
    }

    public static <T> ResultEntity<T> failed(String msg){
        return new ResultEntity<T>(fail,msg,null);
    }
}