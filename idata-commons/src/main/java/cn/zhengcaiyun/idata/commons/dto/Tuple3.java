package cn.zhengcaiyun.idata.commons.dto;

public class Tuple3<T1, T2, T3> {
    public final T1 _f1;
    public final T2 _f2;
    public final T3 _f3;

    public Tuple3(T1 _f1, T2 _f2, T3 _f3) {
        this._f1 = _f1;
        this._f2 = _f2;
        this._f3 = _f3;
    }
}