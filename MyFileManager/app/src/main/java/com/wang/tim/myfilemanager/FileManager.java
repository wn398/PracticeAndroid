package com.wang.tim.myfilemanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.AlertDialog.Builder;
import static android.app.AlertDialog.OnCancelListener;
import static android.app.AlertDialog.OnClickListener;

/**
 * Created by twang on 2014/12/19.
 */
public class FileManager extends ListActivity {
    private List<SingleFileData> directoryEntries = new ArrayList<SingleFileData>();
    private File currentDirectory = new File("/");
    private File tempFile = null;
    private int  temOpt = -1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showToRoot();
        this.setSelection(0);
    }
    //浏览文件系统根目录
    private void showToRoot(){
        showTo(new File("/"));
    }
    //返回上一级目录
    private void upOneLevel(){
        if(this.currentDirectory.getParent()!=null){
            this.showTo(this.currentDirectory.getParentFile());
        }
    }
    //浏览指定目录
    private void showTo(File file){
        this.setTitle(file.getAbsolutePath());
        if(file.isDirectory()){
            this.currentDirectory = file;
            fill(file.listFiles());
        }else {
            fileOperation(file);
        }
    }
    //通过文件名判断是什么类型的文件
    private boolean checkEndsWithInStringArray(String checkItsEnd,String[] fileEndings) {
        for (String end : fileEndings) {
            if (checkItsEnd.endsWith(end)) {
                return true;
            }
        }
        return false;
    }
    //打开指定文件
    protected void openFile(File afile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(afile.getAbsolutePath());
        //取得文件名
        String fileName = file.getName();
        //根据不同类型来找开文件
        if(checkEndsWithInStringArray(fileName,getResources().getStringArray(R.array.fileEndingImage))) {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        else if (checkEndsWithInStringArray(fileName, getResources().getStringArray((R.array.fileEndingAudio)))) {
            intent.setDataAndType(Uri.fromFile(file),"audio/*");
        }
        else if(checkEndsWithInStringArray(fileName,getResources().getStringArray(R.array.fileEndingVideo))) {
            intent.setDataAndType(Uri.fromFile(file), "video/*");
        }
        startActivity(intent);
    }

    //设置listActivity
    private void fill(File[] files) {
        //清空列表
        this.directoryEntries.clear();
        //添加一个当前的选项
        this.directoryEntries.add(new SingleFileData(getString(R.string.current_dir),getResources().getDrawable(R.drawable.folder)));
        //如果不是根目录则添加上一级目录项
        if (this.currentDirectory.getParent() != null) {
            this.directoryEntries.add(new SingleFileData(getString(R.string.up_one_level),getResources().getDrawable(R.drawable.uponelevel)));
        }
        Drawable currentIcon = null;
        for (File currentFile : files) {
            //判断是一个文件夹还是文件
            if (currentFile.isDirectory()) {
                currentIcon = getResources().getDrawable(R.drawable.folder);
            }else{
                //取得文件名
                String fileName = currentFile.getName();
                //根据文件名来判断文件类型，设置不同图标
                if(checkEndsWithInStringArray(fileName,getResources().getStringArray(R.array.fileEndingImage))) {
                    currentIcon = getResources().getDrawable(R.drawable.image);
                }
                else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
                {
                    currentIcon = getResources().getDrawable(R.drawable.webtext);
                }
                else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
                {
                    currentIcon = getResources().getDrawable(R.drawable.packed);
                }
                else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
                {
                    currentIcon = getResources().getDrawable(R.drawable.audio);
                }
                else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
                {
                    currentIcon = getResources().getDrawable(R.drawable.video);
                }
                else
                {
                    currentIcon = getResources().getDrawable(R.drawable.text);
                }
            }
            //确保只显示文件名，不显示路径
            int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
            this.directoryEntries.add(new SingleFileData(currentFile.getAbsolutePath().substring(currentPathStringLenght), currentIcon));
        }
        Collections.sort(this.directoryEntries);
        FileManagerAdapter fileManagerAdapter = new FileManagerAdapter(this);
        //将表设置进去
        fileManagerAdapter.setItems(this.directoryEntries);
        //添加lsitAdapter
        this.setListAdapter(fileManagerAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //取得选中的一项的文件名
        String selectedFileName = this.directoryEntries.get(position).getFileName();
        //选中的是当前目录
        if(selectedFileName.equals(getString(R.string.current_dir))){
            //如果选中就是刷新
            this.showTo(this.currentDirectory);
        }
        //选中的是返回上级目录
        else if(selectedFileName.equals(getString(R.string.up_one_level))){
            //返回上级目录
            this.upOneLevel();
        }
        else{
            File clickedFile = null;
            clickedFile = new File(this.currentDirectory.getAbsolutePath()+this.directoryEntries.get(position).getFileName());
            if(clickedFile != null){
                this.showTo(clickedFile);
            }
        }
    }

    public boolean onCreateOptionMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "新建目录").setIcon(R.drawable.addfolderr);
        menu.add(0, 1, 0, "删除目录").setIcon(R.drawable.delete);
        menu.add(0, 2, 0, "粘贴文件").setIcon(R.drawable.paste);
        menu.add(0, 3, 0, "根目录").setIcon(R.drawable.goroot);
        menu.add(0, 4, 0, "上一级").setIcon(R.drawable.uponelevel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case 0:
                Mynew();
                break;
            case 1:
                //删除
                MyDelate();
                break;
            case 2:
                MyPaste();
                break;
            case 3:
                this.showToRoot();
                break;
            case 4:
                this.upOneLevel();
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    //得到当前目录的绝对路径
    public String GetCurDirectory() {
        return this.currentDirectory.getAbsolutePath();
    }
    //粘贴操作
    public void MyPaste() {
        if(tempFile == null){
            Builder builder = new Builder(FileManager.this);
            builder.setTitle("提示");
            builder.setMessage("没有复制或剪切操作");
            builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        }
        else{
            if( temOpt == 0)//复制
            {
                if(new File(GetCurDirectory()+"/"+tempFile.getName()).exists()) {
                    Builder builder = new Builder(FileManager.this);
                    builder.setTitle("粘贴提示");
                    builder.setMessage("该目录有相同的文件，是否覆盖？");
                    builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveFile(tempFile.getAbsolutePath(),GetCurDirectory()+"/"+tempFile.getName());
                            showTo(new File(GetCurDirectory()));
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel,new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();

                }else
                {
                    moveFile(tempFile.getAbsolutePath(),GetCurDirectory()+"/"+tempFile.getName());
                    showTo(new File(GetCurDirectory()));
                }
            }
        }
    }

    //删除整个文件夹
    public void MyDelate() {
        //取得当前目录
        File tmp = new File(this.currentDirectory.getAbsolutePath());
        //跳到上一级目录
        this.upOneLevel();
        //删除取得的目录
        if(deleteFolder(tmp)) {
            Builder builder = new Builder(FileManager.this);
            builder.setTitle("提示");
            builder.setMessage("删除成功");
            builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        }else {
            Builder builder = new Builder(FileManager.this);
            builder.setTitle("提示");
            builder.setMessage("删除失败");
            builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        }
        this.showTo(this.currentDirectory);
    }

    //新建文件夹
    public void Mynew() {
        LayoutInflater factory = LayoutInflater.from(FileManager.this);
        final View dialogview = factory.inflate(R.layout.dialog,null);
        //设置TextView
        ((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText("请输入新建文件夹名称!");
        //设置EditText
        ((EditText)dialogview.findViewById(R.id.EditText_PROM)).setText("文件夹名称");
        Builder builder = new Builder(FileManager.this);
        builder.setTitle("新建文件夹");
        builder.setView(dialogview);
        builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
                if (newFolder(value)) {
                    Builder builder = new Builder(FileManager.this);
                    builder.setTitle("提示");
                    builder.setMessage("新建文件夹成功");
                    builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //点击确定按键后
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }else {
                    Builder builder = new Builder(FileManager.this);
                    builder.setTitle("提示");
                    builder.setMessage("新建文件夹失败");
                    builder.setPositiveButton(android.R.string.ok,new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //新建文件夹
    public boolean newFolder(String file) {
        File dirFile = new File(this.currentDirectory.getAbsolutePath() + "/" + file);
        try {
            if ((!dirFile.exists()) && !(dirFile.isDirectory())) {
                boolean creatOk = dirFile.mkdirs();
                if (creatOk) {
                    this.showTo(this.currentDirectory);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除文件
    public boolean deleteFile(File file) {
        boolean result = false;
        if (file != null) {
            try{
                File file2 = file;
                file2.delete();
                result = true;
            }catch (Exception e){
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    //删除文件夹
    public boolean deleteFolder(File folder) {
        boolean result = false;
        try {
            String childs[] = folder.list();
            if (childs == null || childs.length <= 0) {
                if (folder.delete()) {
                    result = true;
                }
            } else {
                for (int i = 0; i < childs.length; i++) {
                    String childName = childs[i];
                    String childPath = folder.getPath() + File.separator + childName;
                    File filePath = new File(childPath);
                    if (filePath.exists() && filePath.isFile()) {
                        if (filePath.delete()) {
                            result = true;
                        } else {
                            result = false;
                            break;
                        }
                    } else if (filePath.exists() && filePath.isDirectory()) {
                        if (deleteFolder(filePath)) {
                            result = true;
                        } else {
                            result = false;
                            break;
                        }
                    }
                }
                folder.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    //处理文件打开，生命名等操作
    public void fileOperation(final File file) {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openFile(file);
                }else if(which == 1) {
                    //自定义一个带输入的对话框由TextView和EditText构成
                    LayoutInflater factory = LayoutInflater.from(FileManager.this);
                   final View dialogview = factory.inflate(R.layout.rename,null);
                    //设置TextView的提示信息
                    ((TextView) dialogview.findViewById(R.id.TextView01)).setText("重命名");
                    //设置EditText输入框初始值
                    ((EditText) dialogview.findViewById(R.id.EditText01)).setText(file.getName());

                    Builder builder = new Builder(FileManager.this);
                    builder.setTitle("重命名");
                    builder.setView(dialogview);
                    builder.setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定之后
                                    String value = GetCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
                                    if(new File(value).exists())
                                    {
                                        Builder builder = new Builder(FileManager.this);
                                        builder.setTitle("重命名");
                                        builder.setMessage("文件名重复，是否需要覆盖？");
                                        builder.setPositiveButton(android.R.string.ok,
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String str2 = GetCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
                                                        file.renameTo(new File(str2));
                                                        showTo(new File(GetCurDirectory()));
                                                    }
                                                });
                                        builder.setNegativeButton(android.R.string.cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder.setCancelable(false);
                                        builder.create();
                                        builder.show();
                                    }
                                    else
                                    {
                                        //重命名
                                        file.renameTo(new File(value));
                                        showTo(new File(GetCurDirectory()));
                                    }
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else if ( which == 2 )
                {
                    Builder builder = new Builder(FileManager.this);
                    builder.setTitle("删除文件");
                    builder.setMessage("确定删除"+file.getName()+"？");
                    builder.setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if ( deleteFile(file) )
                                    {
                                        Builder builder = new Builder(FileManager.this);
                                        builder.setTitle("提示对话框");
                                        builder.setMessage("删除成功");
                                        builder.setPositiveButton(android.R.string.ok,
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定按钮之后
                                                        dialog.cancel();
                                                        showTo(new File(GetCurDirectory()));
                                                    }
                                                });
                                        builder.setCancelable(false);
                                        builder.create();
                                        builder.show();
                                    }
                                    else
                                    {
                                        Builder builder = new Builder(FileManager.this);
                                        builder.setTitle("提示对话框");
                                        builder.setMessage("删除失败");
                                        builder.setPositiveButton(android.R.string.ok,
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定按钮之后
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder.setCancelable(false);
                                        builder.create();
                                        builder.show();
                                    }
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
                else if ( which == 3 )//复制
                {
                    //保存我们复制的文件目录
                    tempFile = file;
                    //这里我们用0表示复制操作
                    temOpt = 0;
                }
                else if ( which == 4 )//剪切
                {
                    //保存我们复制的文件目录
                    tempFile = file;
                    //这里我们用0表示剪切操作
                    temOpt = 1;
                }
            }
        };
        //显示操作菜单
        String[] menu={"打开","重命名","删除","复制","剪切"};
        new AlertDialog.Builder(FileManager.this)
                .setTitle("请选择你要进行的操作")
                .setItems(menu,listener)
                .show();

                }

    //移动文件
    public void moveFile(String source, String destination)
    {
        new File(source).renameTo(new File(destination));
    }
    //复制文件
    public void copyFile(File src, File target)
    {
        InputStream in = null;
        OutputStream out = null;

        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        try
        {
            in = new FileInputStream(src);
            out = new FileOutputStream(target);
            bin = new BufferedInputStream(in);
            bout = new BufferedOutputStream(out);

            byte[] b = new byte[8192];
            int len = bin.read(b);
            while (len != -1)
            {
                bout.write(b, 0, len);
                len = bin.read(b);
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bin != null)
                {
                    bin.close();
                }
                if (bout != null)
                {
                    bout.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
