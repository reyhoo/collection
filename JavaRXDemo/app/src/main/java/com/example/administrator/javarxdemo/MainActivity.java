package com.example.administrator.javarxdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    static List<Activity> list= new ArrayList<>();
    private Subscription subscription,subscription1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.msgTv);
        tv.setText("");
        String[] abis = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        }else{
            abis = new String[]{Build.CPU_ABI,Build.CPU_ABI2};
        }
        for (String s :
                abis) {
            tv.append(s);
            tv.append("\n");
        }
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics m = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(m);
        tv.append("屏幕信息：\n");
        tv.append("density:"+m.density + "\n");
        tv.append("widthPixels:"+m.widthPixels + "\n");
        tv.append("heightPixels:"+m.heightPixels + "\n");
        tv.append("densityDpi:"+m.densityDpi + "\n");
        tv.append("scaledDensity:"+m.scaledDensity + "\n");
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
        int height = drawable.getBitmap().getHeight();
        String dpi = "";
        switch (height){
            case 192:
                dpi = "xxxhdpi";//4  640
                break;
            case 144:
                dpi = "xxhdpi";//3  480
                break;
            case 96:
                dpi = "xhdpi";//2  320
                break;
            case 72:
                dpi = "hdpi";//1.5 240
                break;
            case 48:
                dpi = "mdpi";//1  160
                break;
        }
        tv.append("dpi:"+dpi+ "\n");
//        list.add(this);
        //just
//        Observable.just("1","2","3").subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.i("hello_","hello_:"+s);
//            }
//        });
//        List<String>list = new ArrayList<>();
//        list.add("yes1");
//        list.add("yes2");
//        list.add("yes3");
//        //from
//        Observable.from(list).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.i("hello_","hello_onNext:"+Thread.currentThread()+s);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                Log.i("hello_","hello_onError:"+throwable);
//            }
//        }, new Action0() {
//            @Override
//            public void call() {
//                Log.i("hello_","hello_onComplete"+Thread.currentThread());
//            }
//        });
        //create
//        Observable.create(new Observable.OnSubscribe<String>(){
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext(""+new Date());
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                Log.i("hello_","hello_onComplete"+Thread.currentThread());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("hello_","hello_onError"+Thread.currentThread());
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.i("hello_","hello_onNext:"+Thread.currentThread()+":"+s);
//            }
//        });


//       final Observable observable =  Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                subscriber.onNext(111);
//                Log.i("hello_","hello_observable:call:"+Thread.currentThread());
//            }
//        }).subscribeOn(Schedulers.io())
//               .doOnSubscribe(new Action0() {
//                   @Override
//                   public void call() {
//                       Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();
//                   }
//               }).subscribeOn(AndroidSchedulers.mainThread())
//               .observeOn(Schedulers.io())
//               .map(new Func1<Integer, String>() {
//                   @Override
//                   public String call(Integer integer) {
//                       Log.i("hello_","hello_map:call:"+Thread.currentThread());
//                       return integer+"";
//                   }
//               })
//               .observeOn(AndroidSchedulers.mainThread())
//                .lift(new Observable.Operator<Integer, String>() {
//            @Override
//            public Subscriber<? super String> call(final Subscriber<? super Integer> subscriber) {
//                return new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hello_","hello_String:onCompleted:"+Thread.currentThread());
//                        subscriber.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hello_","hello_String:onError:"+e+Thread.currentThread());
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.i("hello_","hello_String:onNext:"+s+Thread.currentThread());
//                        try{
//                            subscriber.onNext(Integer.parseInt(s));
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            subscriber.onError(e);
//                        }
//
//                    }
//
//                    @Override
//                    public void onStart() {
////                        Log.i("hello_","hello_String:onStart:"+Thread.currentThread());
//                        subscriber.onStart();
//                    }
//                };
//            }
//        });
//        new Thread("My Thread"){
//            @Override
//            public void run() {
//                observable.subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hello_","hello_Integer:onCompleted:"+Thread.currentThread());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hello_","hello_Integer:onError:"+e+Thread.currentThread());
//                    }
//
//                    @Override
//                    public void onNext(Integer s) {
//                        Log.i("hello_","hello_Integer:onNext:"+s+Thread.currentThread());
//                    }
//
//                    @Override
//                    public void onStart() {
////                        Log.i("hello_","hello_Integer:onStart:"+Thread.currentThread());
//                        super.onStart();
//                    }
//                });
//            }
//        }.start();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final List<Student>list = new ArrayList<Student>();
                Student stu = new Student();
                list.add(stu);
                stu.name = "stu1";
                stu.courses.add(new Course("stu1==course1"));
                stu.courses.add(new Course("stu1==course2"));
                stu.courses.add(new Course("stu1==course3"));
                stu = new Student();
                list.add(stu);
                stu.name = "stu2";
                stu.courses.add(new Course("stu2==course1"));
                stu.courses.add(new Course("stu2==course2"));
                stu.courses.add(new Course("stu2==course3"));
                subscription = Observable.create(new Observable.OnSubscribe<Student>() {
                    @Override
                    public void call(Subscriber<? super Student> subscriber) {
                        Log.i("hello_","hello_:"+Thread.currentThread()+";;OnSubscribe");
                        //工作线程

                        for (Student student :
                                list) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext(student);
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            //UI线程
                            DialogUtil.showLoading(MainActivity.this, "loading...", new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if(subscription!=null && !subscription.isUnsubscribed()){
                                        subscription.unsubscribe();
                                    }
                                }
                            },true);
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                   .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(final Student student) {
                        //UI线程
                        Log.i("hello_","hello_:"+Thread.currentThread()+";;flatMap");
                        return Observable.create(new Observable.OnSubscribe<Course>() {
                            @Override
                            public void call(Subscriber<? super Course> subscriber) {
                                //UI线程
                                Log.i("hello_","hello_:"+Thread.currentThread()+";;flatMap;;OnSubscribe");
                                for (Course course :
                                        student.courses) {
                                    subscriber.onNext(course);
                                }
                                subscriber.onCompleted();
                            }
                        })
                                //.subscribeOn(Schedulers.io())//如果这里切换到io线程，那后面的map,subscribe都会在io线程；调用observeOn(下行)切换到UI线程
                                ;
                    }
                })
//                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Course, String>() {
                    @Override
                    public String call(Course course) {
                        //UI线程
                        Log.i("hello_","hello_:"+Thread.currentThread()+";;map");
                        return course.name;
                    }
                }).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //UI线程
                        Log.i("hello_", "hello_:" + Thread.currentThread() + ";;subscribe:" + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //UI线程
                        DialogUtil.dismissDialog();
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        //UI线程
                        DialogUtil.dismissDialog();
                        Toast.makeText(MainActivity.this, "complete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SecondActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        findViewById(R.id.interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription1 = Observable.interval(1, TimeUnit.SECONDS).filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong >= 5;
                    }
                })
                        .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.i("interval_","interval_"+aLong);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(subscription1!=null && !subscription1.isUnsubscribed()){
            subscription1.unsubscribe();
        }
    }
}
