package gamingbd.pro;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Process;

import static android.view.ViewGroup.LayoutParams.*;
import static gamingbd.pro.Native.*;

public class FloatingViewService extends Service{

    /* Window is the root of everything that floats on screen */
    public WindowManager    Window;

    /* We have a Frame in the Window that will float */
    public FrameLayout      MainFrame;

    /*We have a Main Layout in the Frame */
    public RelativeLayout   MainLayout;

    /* Main Layout contains Collapsed View (ICON) & Expanded View (Title+Menu+Buttons) */
    public RelativeLayout   collapsedView;
    public LinearLayout     expandedView;

    /* Collapsed View contains only the floating icon */
    public ImageView        Icon;

    /* Expanded View contains Title + Menu + Buttons */
    public TextView         Title;
    public ScrollView       Menu;
    public RelativeLayout   Buttons;

    /* Buttons contain Inject Button & Close Button */
    public Button           InjectButton, CloseButton;

    /* LayoutParameters is used to change the position of the Window (Whole Floating Stuffs) */
    public WindowManager.LayoutParams LayoutParameters;

    /* Floating icon inside the Collapsed View. The icon image is stored in Base64 encoded format */
    public String iconString = IconNative();



    /* This method is called when the service is created */
    @Override
    public void onCreate() {
        super.onCreate();
        StartFloat();
    }


    private void StartFloat() {

        /* Layout Scheme :

            Window [Window] {
                Main Frame [FrameLayout] {
                    Main Layout [RelativeLayout] {
                        Collapsed View [RelativeLayout]{
                            Icon [ImageView]
                        }
                        Expanded  View [LinearLayout]{
                            Title [TextView]
                            Menu  [ScrollView]{

                            }
                            Buttons [RelativeLayout]{
                                InjectButton [Button]
                                CloseButton  [Button]
                            }
                        }
                    }
                }
            }

         */


        /** Main Frame [FrameLayout] **/
        {
            /* Frame holds the entire floating view */
            MainFrame = new FrameLayout(this);
            MainFrame.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

            /** Main Layout [RelativeLayout] */
            {
                /* Creating the Main Layout inside the Frame */
                /* MainLayout contains the collapsed view (ICON) & the expanded view (MENU) */
                MainLayout = (new RelativeLayout(this));
                MainLayout.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                /** Collapsed View [RelativeLayout] */
                {
                    /*---------------------- (Collapsed View) or (Floating Icon View) */
                    /* When we close/collapse the floating menu this collapsedView will be shown */
                    /* Initially the floating view should only show the icon [collapsed view] */
                    collapsedView = new RelativeLayout(this);
                    collapsedView.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                    collapsedView.setVisibility(View.VISIBLE);

                    /* Icon [ImageView] */
                    {
                        /* In the collapsed view there will be only an image as icon */
                        Icon = new ImageView(this);
                        Icon.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                        /* Icon size 50 dp */
                        int applyDimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 50, getResources().getDisplayMetrics());
                        Icon.getLayoutParams().height = applyDimension;
                        Icon.getLayoutParams().width  = applyDimension;
                        Icon.setScaleType(ImageView.ScaleType.FIT_XY);
                        byte[] decode = Base64.decode( iconString, 0);
                        Icon.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));

                        Icon.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                /* Hide icon & show expanded view */
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }

                /** Expanded  View [LinearLayout] */
                {
                    /* Initially the expanded view will be hidden */
                    expandedView = new LinearLayout(this);
                    expandedView.setVisibility(View.GONE);
                    expandedView.setOrientation(LinearLayout.VERTICAL);
                    /** Expanded View Width 400 dp **/
                    expandedView.setLayoutParams(new LinearLayout.LayoutParams(pixelToDp(400), WRAP_CONTENT));

                    {
                        /** Title [TextView] */
                        {
                            Title = new TextView(this);
                            Title.setText("gamingbd.pro");
                            Title.setTextColor(Color.WHITE);
                            Title.setTextSize(18);
                            Title.setGravity(Gravity.CENTER);
                            Title.setTypeface(null, Typeface.BOLD);
                            Title.setPadding(0, 15, 0, 15);
                            Title.setBackgroundColor(Color.parseColor("#3A833A"));
                        }

                        /** Menu [ScrollView] */
                        {
                            Menu = new ScrollView(this);
                            /** Menu height 200 dp **/
                            Menu.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, pixelToDp(200)));
                            Menu.setPadding(pixelToDp(5), pixelToDp(5), pixelToDp(5), pixelToDp(5));
                            Menu.setBackgroundColor(Color.BLACK);
                            GradientDrawable gradientDrawable = new GradientDrawable();
                            gradientDrawable.setColor(Color.BLACK);
                            gradientDrawable.setCornerRadius(0);
                            /** Semi Transparent, Alpha Range: 0-255 **/
                            gradientDrawable.setAlpha(128);
                            Menu.setBackground(gradientDrawable);


                        }

                        /** Buttons [RelativeLayout]  */
                        {
                            Buttons = new RelativeLayout(this);
                            Buttons.setPadding(5, 15, 5, 15);
                            Buttons.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                            Buttons.setVerticalGravity(Gravity.CENTER);
                            Buttons.setBackgroundColor(Color.BLACK);

                            /** InjectButton [Button] **/
                            {
                                InjectButton = new Button(this);
                                InjectButton.setText("Inject");
                                InjectButton.setAllCaps(false);
                                InjectButton.setTextColor(Color.parseColor("#8C8D92"));
                                InjectButton.setTypeface(null, Typeface.BOLD);
                                InjectButton.setTextSize(14);
                                /* Button Height 40 dp */
                                InjectButton.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, pixelToDp(40)));

                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setColor(Color.parseColor("#3A833A"));
                                gradientDrawable.setCornerRadius(100);
                                InjectButton.setBackground(gradientDrawable);
                                InjectButton.setTextColor(Color.WHITE);

                                InjectButton.setOnClickListener(new View.OnClickListener() {
                                    @Override public void onClick(View view) {

                                        /* Inject payload */
                                        boolean injected = Utils.inject(getApplicationContext());
                                        if ( injected ){
                                            try{
                                                Process.killProcess(Process.myPid());
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }

                            /** CloseButton  [Button] **/
                            {
                                CloseButton = new Button(this);
                                CloseButton.setText("Close");
                                CloseButton.setAllCaps(false);
                                CloseButton.setTextColor(Color.parseColor("#8C8D92"));
                                CloseButton.setTypeface(null, Typeface.BOLD);
                                CloseButton.setTextSize(14);

                                /* Button Height 40 dp */
                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, pixelToDp(40));
                                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                CloseButton.setLayoutParams(layoutParams);

                                GradientDrawable gradientDrawable = new GradientDrawable();
                                gradientDrawable.setColor(Color.parseColor("#D4403A"));
                                gradientDrawable.setCornerRadius(100);
                                CloseButton.setBackground(gradientDrawable);
                                CloseButton.setTextColor(Color.WHITE);

                                CloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override public void onClick(View view) {
                                        /** Hide expanded view & Show collapsed view (icon) **/
                                        collapsedView.setVisibility(View.VISIBLE);
                                        expandedView.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        /* Lets add components to the layout */

        /** Main Frame [FrameLayout] */
        {
            /* Adding Main Layout to the Frame */
            MainFrame.addView(MainLayout);

            /** Main Layout [RelativeLayout] */
            {
                /* Adding Collapsed View(ICON), Expanded View ( Title + Menu + Bottom Buttons ) to Main Layout */
                MainLayout.addView(collapsedView);
                MainLayout.addView(expandedView);

                /** Collapsed View [RelativeLayout] */
                {
                    /* Adding ICON in Collapsed View */
                    collapsedView.addView(Icon);
                }

                /** Expanded  View [LinearLayout] */
                {
                    /* Adding Title in Expanded View ( Title + Menu + Bottom Buttons ) */
                    expandedView.addView(Title);
                    expandedView.addView(Menu);
                    expandedView.addView(Buttons);


                    /** Buttons [RelativeLayout]  */
                    {
                        /* Adding Inject/Close Button */
                        Buttons.addView(InjectButton);
                        Buttons.addView(CloseButton);
                    }

                }
            }
        }

        Window                   =   (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutParameters         =   new WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 2038 : 2002, 8, -3);
        LayoutParameters.gravity =   Gravity.TOP | Gravity.LEFT;
        LayoutParameters.x       =   0;
        LayoutParameters.y       =   100;
        Window.addView(MainFrame, LayoutParameters);
        MainFrame.setOnTouchListener(onTouchListener());
        Icon.setOnTouchListener(onTouchListener());
    }

    /** Handle Touch Event */
    public View.OnTouchListener onTouchListener() {

        return new View.OnTouchListener() {

            final View collapsedView    = FloatingViewService.this.collapsedView;
            final View expandedView     = FloatingViewService.this.expandedView;

            float   initialTouchX;
            float   initialTouchY;
            int     initialX;
            int     initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        initialX        = LayoutParameters.x;
                        initialY        = LayoutParameters.y;
                        initialTouchX   = motionEvent.getRawX();
                        initialTouchY   = motionEvent.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:

                        int rawX = (int) (motionEvent.getRawX() - initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - initialTouchY);

                        if ( rawX < 10 && rawY < 10 && isViewCollapsed() ){
                            try {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        /* Make Layout Less Transparent If Dragging Stops */
                        {
                            GradientDrawable gradientDrawable = new GradientDrawable();
                            gradientDrawable.setColor(Color.BLACK);
                            gradientDrawable.setAlpha(200);         // Alpha [0 -> 255] = More Dark
                            Menu.setBackground(gradientDrawable);
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:

                        LayoutParameters.x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                        LayoutParameters.y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));
                        Window.updateViewLayout(MainFrame, LayoutParameters);

                        /** Make Layout More Transparent While Dragging **/
                        {
                            GradientDrawable gradientDrawable   = new GradientDrawable();
                            gradientDrawable.setColor(Color.BLACK);
                            gradientDrawable.setAlpha(100);         // Alpha [0 -> 255] = More Dark
                            Menu.setBackground(gradientDrawable);
                        }
                        return true;


                    default:
                        return false;
                }
            }
        };
    }

    /* Check if view is collapsed or expanded */
    public boolean isViewCollapsed() {
        return MainFrame == null || collapsedView.getVisibility() == View.VISIBLE;
    }

    /* Convert dp to pixel */
    public int pixelToDp(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, getResources().getDisplayMetrics());
    }

    /* Interface(s) */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}



