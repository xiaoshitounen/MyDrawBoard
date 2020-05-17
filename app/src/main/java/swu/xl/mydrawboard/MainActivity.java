package swu.xl.mydrawboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import swu.xl.bottomview.XLBottomView;
import swu.xl.utils.SaveToAlbumUtil;
import swu.xl.xldrawboard.XLDrawBoard;
import swu.xl.xlseekbar.XLSeekBar;

public class MainActivity extends AppCompatActivity {

    private XLSeekBar size_controller;
    private XLDrawBoard draw_board;
    private XLBottomView operation_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化操作
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        size_controller = findViewById(R.id.size_controller);
        draw_board = findViewById(R.id.draw_board);
        operation_controller = findViewById(R.id.bottom_view);

        //字体大小控制器
        size_controller.setCurrentProgress(draw_board.getLineWidth());
        size_controller.setProgressChangeListener(new XLSeekBar.OnProgressChangeListener() {
            @Override
            public void progressChanged(float progress) {
                draw_board.setLineWidth((int) progress);
            }
        });

        //画板
        draw_board.setLineColor(Color.BLACK);

        //底部控制器
        List<XLBottomView.BottomViewItem> items = new ArrayList<>();
        int[] icon_ids = {R.drawable.pen,R.drawable.eraser,R.drawable.undo,R.drawable.redo,R.drawable.clear,R.drawable.save};
        String[] titles = {"画笔","橡皮","撤销","重做","清空","保存"};
        for (int i = 0; i < 6; i++) {
            XLBottomView.BottomViewItem item = new XLBottomView.BottomViewItem(icon_ids[i], titles[i]);
            items.add(item);
        }
        operation_controller.setItems(items);
        operation_controller.setXLBottomViewItemListener(new XLBottomView.XLBottomViewItemListener() {
            @Override
            public void itemStatusDidChange(int index) {
                switch (index){
                    case 0:
                        //橡皮
                        draw_board.setBoard_state(XLDrawBoard.BOARD_STATE_PEN);
                        break;
                    case 1:
                        //橡皮
                        draw_board.setBoard_state(XLDrawBoard.BOARD_STATE_ERASER);
                        break;
                    case 2:
                        //撤销
                        draw_board.removeLast();
                        break;
                    case 3:
                        //重做
                        draw_board.resumeLast();
                        break;
                    case 4:
                        //清空
                        draw_board.removeAll();
                        break;
                    case 5:
                        //根据时间戳命名
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                        Date date = new Date(System.currentTimeMillis());
                        String fileName = simpleDateFormat.format(date);

                        //保存
                        Bitmap bitmap = draw_board.save();
                        SaveToAlbumUtil.saveBitmapToGallery(
                                bitmap,
                                fileName,
                                MainActivity.this
                        );
                        break;
                }
            }
        });
    }

    //设置画笔颜色
    public void changeColor(View v){
        //获取按钮的背景颜色
        ColorDrawable drawable = (ColorDrawable) v.getBackground();

        //设置背景颜色
        draw_board.setLineColor(drawable.getColor());
    }
}
