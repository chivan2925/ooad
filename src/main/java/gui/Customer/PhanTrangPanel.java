package gui.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhanTrangPanel extends JPanel {
    private JButton btnFirst;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnLast;
    private JLabel lblPageInfo;
    
    private SanPhamPanel parentPanel;
    private int currentPage = 1;
    private int totalPages = 1;
    
    public PhanTrangPanel(SanPhamPanel parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnFirst = new JButton("<<");
        btnFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    parentPanel.showPage(1);
                }
            }
        });
        
        btnPrevious = new JButton("<");
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    parentPanel.showPage(currentPage - 1);
                }
            }
        });
        
        lblPageInfo = new JLabel("Trang 1/1");
        
        btnNext = new JButton(">");
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < totalPages) {
                    parentPanel.showPage(currentPage + 1);
                }
            }
        });
        
        btnLast = new JButton(">>");
        btnLast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < totalPages) {
                    parentPanel.showPage(totalPages);
                }
            }
        });
        
        add(btnFirst);
        add(btnPrevious);
        add(lblPageInfo);
        add(btnNext);
        add(btnLast);
        
        updateButtonStates();
    }
    
    public void updatePageInfo(int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        lblPageInfo.setText(String.format("Trang %d/%d", currentPage, totalPages));
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        btnFirst.setEnabled(currentPage > 1);
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);
    }
}