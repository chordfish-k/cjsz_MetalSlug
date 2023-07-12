package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @˵�� �����࣬���ڼ����û��Ĳ��� KeyListener
 * @author renjj
 *
 */
public class GameListener implements KeyListener{
	private ElementManager em=ElementManager.getManager();
	
	/*�ܷ�ͨ��һ����������¼���а��µļ�������ظ���������ֱ�ӽ���
	 * ͬʱ����1�ΰ��£���¼�������У���2���ж������з��С�
	 *       �ɿ���ֱ��ɾ�������еļ�¼��
	 * set����
	 * */
	private Set<Integer> set=new HashSet<Integer>();
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	/**
	 * ����: ��37 ��38 ��39 ��40    ��tabû�з�Ӧ
	 * ʵ�����ǵ��ƶ�
	 */
	@Override
	public void keyPressed(KeyEvent e) {
//		�õ���Ҽ���
		System.out.println("����"+e.getKeyCode());
		int key=e.getKeyCode();
		if(set.contains(key)) { //�ж��������Ƿ��Ѿ�����,�����������
//			�������ֱ�ӽ�������
			return;
		}
		set.add(key);
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.keyClick(true, e.getKeyCode());
		}
	}
	/**�ɿ�*/
	@Override
	public void keyReleased(KeyEvent e) {
		if(!set.contains(e.getKeyCode())) {//�����������ڣ���ֹͣ
			return;
		}//����(�Ѿ������������)
		set.remove(e.getKeyCode());//�Ƴ�����
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.keyClick(false, e.getKeyCode());
		}
	}

}
