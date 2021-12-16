import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;

public class Ct extends JFrame {
    public static void main(String[] args) throws IOException {
        new Ct();
    }
    private JScrollPane jsp;
    private JPanel jp;
    private JTextArea jta;
    private JTextField jtf;
    private JButton jbt;
    BufferedOutputStream bos = null;

    public Ct() throws IOException {
        jta = new JTextArea();
        jsp = new JScrollPane(jta, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jtf = new JTextField(20);
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    System.out.println("回车键事件");
                    sendMes();
                }
            }
        });
        jbt = new JButton("发送");
        jbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMes();
            }
        });
        jp = new JPanel();
        jp.add(jtf);
        jp.add(jbt);

        this.add(jsp, BorderLayout.CENTER);
        this.add(jp, BorderLayout.SOUTH);
        this.setSize(500, 500);
        this.setTitle("客户端");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        Socket client = new Socket("localhost", 6666);
        InputStream is = client.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        OutputStream os = client.getOutputStream();
        bos = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int len=0;
        String content = null;
        while((len = bis.read(buffer))!=-1) {
            content = new String(buffer, 0, len);
            jta.append(content + '\n');
        }
    }

    public void sendMes(){
        String str = jtf.getText();
        str = "客户端端发送了：" + str;
        jtf.setText(null);
        jta.append(str + '\n');
        try {
            bos.write(str.getBytes(), 0, str.getBytes().length);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
