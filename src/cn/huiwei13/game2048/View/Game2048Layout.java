package cn.huiwei13.game2048.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 2048����Ϸ��壬���벼���ļ����ɿ�ʼ��Ϸ
 * 
 * 
 */
public class Game2048Layout extends RelativeLayout
{

	/**
	 * ����Item������n*n��Ĭ��Ϊ4
	 */
	private int mColumn = 5;
	/**
	 * ������е�Item
	 */
	private Game2048Item[] mGame2048Items;

	/**
	 * Item����������ı߾�
	 */
	private int mMargin = 10;
	/**
	 * ����padding
	 */
	private int mPadding;
	/**
	 * ����û�����������
	 */
	private GestureDetector mGestureDetector;

	// ����ȷ���Ƿ���Ҫ����һ���µ�ֵ
	private boolean isMergeHappen = true;
	private boolean isMoveHappen = true;

	/**
	 * ��¼����
	 */
	private int mScore;

	public interface OnGame2048Listener
	{
		void onScoreChange(int score);

		void onGameOver();
	}

	private OnGame2048Listener mGame2048Listener;

	public void setOnGame2048Listener(OnGame2048Listener mGame2048Listener)
	{
		this.mGame2048Listener = mGame2048Listener;
	}

	/**
	 * �˶������ö��
	 * 
	 * 
	 */
	private enum ACTION
	{
		LEFT, RIGHT, UP, DOWM
	}

	public Game2048Layout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mMargin, getResources().getDisplayMetrics());
		// ����Layout���ڱ߾࣬�ı�һ�£�����Ϊ���ڱ߾��е���Сֵ
		mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
				getPaddingBottom());

//		mGestureDetector = new GestureDetector(context , new MyGestureDetector());
		setOnTouchListener(new OnTouchListener() {
			private float startX,startY,offsetX,offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = e.getX();
					startY = e.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = e.getX() - startX;
					offsetY = e.getY() - startY;
					
					if(Math.abs(offsetX) > Math.abs(offsetY)){
						if(offsetX < -5){
							//����
							action(ACTION.LEFT);
						}else if(offsetX > 5){
							//����
							action(ACTION.RIGHT);
						}
						
					}else{
						if(offsetY < -5){
							//����
							action(ACTION.UP);
						}else if(offsetY > 5){
							//����
							action(ACTION.DOWM);
						}
						
					}
					break;
				
				}
				return true;
			}
		});
	}

	

	/**
	 * �õ���ֵ�е���Сֵ
	 * 
	 * @param params
	 * @return
	 */
	private int min(int... params)
	{
		int min = params[0];
		for (int param : params)
		{
			if (min > param)
			{
				min = param;
			}
		}
		return min;
	}

	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event)
//	{
//		mGestureDetector.onTouchEvent(event);
//		return true;
//	}
	
//	class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
//	{
//
//		final int FLING_MIN_DISTANCE = 50;
//
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY)
//		{
//			float x = e2.getX() - e1.getX();
//			float y = e2.getY() - e1.getY();
//
//			if (x > FLING_MIN_DISTANCE
//					&& Math.abs(velocityX) > Math.abs(velocityY))
//			{
//				action(ACTION.RIGHT);
//				// Toast.makeText(getContext(), "toRight",
//				// Toast.LENGTH_SHORT).show();
//
//			} else if (x < -FLING_MIN_DISTANCE
//					&& Math.abs(velocityX) > Math.abs(velocityY))
//			{
//				action(ACTION.LEFT);
//				// Toast.makeText(getContext(), "toLeft",
//				// Toast.LENGTH_SHORT).show();
//
//			} else if (y > FLING_MIN_DISTANCE
//					&& Math.abs(velocityX) < Math.abs(velocityY))
//			{
//				action(ACTION.DOWM);
//				// Toast.makeText(getContext(), "toDown",
//				// Toast.LENGTH_SHORT).show();
//
//			} else if (y < -FLING_MIN_DISTANCE
//					&& Math.abs(velocityX) < Math.abs(velocityY))
//			{
//				action(ACTION.UP);
//				// Toast.makeText(getContext(), "toUp",
//				// Toast.LENGTH_SHORT).show();
//			}
//			return true;
//
//		}
//	}

	public Game2048Layout(Context context)
	{
		this(context, null);
	}

	public Game2048Layout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	private boolean once;

	/**
	 * ����Layout�Ŀ�͸ߣ��Լ�����Item�Ŀ�͸ߣ��������wrap_content �Կ���֮�е���Сֵ����������
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ��������εı߳�
		int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
		// ���Item�Ŀ��
		int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
				/ mColumn;

		if (!once)
		{
			if (mGame2048Items == null)
			{
				mGame2048Items = new Game2048Item[mColumn * mColumn];
			}
			// ����Item
			for (int i = 0; i < mGame2048Items.length; i++)
			{
				Game2048Item item = new Game2048Item(getContext());

				mGame2048Items[i] = item;
				item.setId(i + 1);
				RelativeLayout.LayoutParams lp = new LayoutParams(childWidth,
						childWidth);
				// ���ú���߾�,�������һ��
				if ((i + 1) % mColumn != 0)
				{
					lp.rightMargin = mMargin;
				}
				// ������ǵ�һ��
				if (i % mColumn != 0)
				{
					lp.addRule(RelativeLayout.RIGHT_OF,
							mGame2048Items[i - 1].getId());
				}
				// ������ǵ�һ�У�//��������߾࣬�����һ��
				if ((i + 1) > mColumn)
				{
					lp.topMargin = mMargin;
					lp.addRule(RelativeLayout.BELOW,
							mGame2048Items[i - mColumn].getId());
				}
				addView(item, lp);
			}
			generateNum();
		}
		once = true;

		setMeasuredDimension(length, length);
	}

	
	
		
		/**
		 * �����û��˶�����������ƶ��ϲ�ֵ��
		 */
		private void action(ACTION action)
		{
			// ��|��
			for (int i = 0; i < mColumn; i++)
			{
				List<Game2048Item> row = new ArrayList<Game2048Item>();
				// ��|��
				//��¼��Ϊ0������
				for (int j = 0; j < mColumn; j++)
				{
					// �õ��±�
					int index = getIndexByAction(action, i, j);

					Game2048Item item = mGame2048Items[index];
					// ��¼��Ϊ0������
					if (item.getNumber() != 0)
					{
						row.add(item);
					}
				}
				
				//�ж��Ƿ����ƶ�
				for (int j = 0; j < mColumn && j < row.size(); j++)
				{
					int index = getIndexByAction(action, i, j);
					Game2048Item item = mGame2048Items[index];

					if (item.getNumber() != row.get(j).getNumber())
					{
						isMoveHappen = true;
					}
				}
				
				// �ϲ���ͬ��
				mergeItem(row);
				
				// ���úϲ����ֵ
				for (int j = 0; j < mColumn; j++)
				{
					int index = getIndexByAction(action, i, j);
					if (row.size() > j)
					{
						mGame2048Items[index].setNumber(row.get(j).getNumber());
					} else
					{
						mGame2048Items[index].setNumber(0);
					}
				}

			}
			//��������
			generateNum();

		}

		/**
		 * ����Action��i,j�õ��±�
		 * 
		 * @param action
		 * @param i
		 * @param j
		 * @return
		 */
		private int getIndexByAction(ACTION action, int i, int j)
		{
			int index = -1;
			switch (action)
			{
			case LEFT:
				index = i * mColumn + j;
				break;
			case RIGHT:
				index = i * mColumn + mColumn - j - 1;
				break;
			case UP:
				index = i + j * mColumn;
				break;
			case DOWM:
				index = i + (mColumn - 1 - j) * mColumn;
				break;
			}
			return index;
		}

		/**
		 * �ϲ���ͬ��Item
		 * 
		 * @param row
		 */
		private void mergeItem(List<Game2048Item> row)
		{
			if (row.size() < 2)
				return;

			for (int j = 0; j < row.size() - 1; j++)
			{
				Game2048Item item1 = row.get(j);
				Game2048Item item2 = row.get(j + 1);

				if (item1.getNumber() == item2.getNumber())
				{
					isMergeHappen = true;

					int val = item1.getNumber() + item2.getNumber();
					item1.setNumber(val);

					// �ӷ�
					mScore += val;
					if (mGame2048Listener != null)
					{
						mGame2048Listener.onScoreChange(mScore);
					}

					// ��ǰ�ƶ�
					for (int k = j + 1; k < row.size() - 1; k++)
					{
						row.get(k).setNumber(row.get(k + 1).getNumber());
					}
					
					row.get(row.size() - 1).setNumber(0);
					return;
				}

			}

		}

	
	
	/**
	 * �Ƿ���������
	 * 
	 * @return
	 */
	private boolean isFull()
	{
		// ����Ƿ�����λ�ö�������
		for (int i = 0; i < mGame2048Items.length; i++)
		{
			if (mGame2048Items[i].getNumber() == 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * ��⵱ǰ���е�λ�ö������֣������ڵ�û����ͬ������
	 * 
	 * @return
	 */
	private boolean checkOver()
	{
		// ����Ƿ�����λ�ö�������
		if (!isFull())
		{
			return false;
		}

		for (int i = 0; i < mColumn; i++)
		{
			for (int j = 0; j < mColumn; j++)
			{

				int index = i * mColumn + j;

				// ��ǰ��Item
				Game2048Item item = mGame2048Items[index];
				// �ұ�
				if ((index + 1) % mColumn != 0)
				{
					Log.e("TAG", "RIGHT");
					// �ұߵ�Item
					Game2048Item itemRight = mGame2048Items[index + 1];
					if (item.getNumber() == itemRight.getNumber())
						return false;
				}
				// �±�
				if ((index + mColumn) < mColumn * mColumn)
				{
					Log.e("TAG", "DOWN");
					Game2048Item itemBottom = mGame2048Items[index + mColumn];
					if (item.getNumber() == itemBottom.getNumber())
						return false;
				}
				// ���
				if (index % mColumn != 0)
				{
					Log.e("TAG", "LEFT");
					Game2048Item itemLeft = mGame2048Items[index - 1];
					if (itemLeft.getNumber() == item.getNumber())
						return false;
				}
				// �ϱ�
				if (index + 1 > mColumn)
				{
					Log.e("TAG", "UP");
					Game2048Item itemTop = mGame2048Items[index - mColumn];
					if (item.getNumber() == itemTop.getNumber())
						return false;
				}

			}

		}

		return true;

	}

	/**
	 * ����һ������
	 */
	public void generateNum()
	{

		if (checkOver())
		{
			Log.e("TAG", "GAME OVER");
			if (mGame2048Listener != null)
			{
				mGame2048Listener.onGameOver();
			}
			return;
		}

		if (!isFull())
		{
			if (isMoveHappen || isMergeHappen)
			{
				Random random = new Random();
				int next = random.nextInt(16);
				Game2048Item item = mGame2048Items[next];

				while (item.getNumber() != 0)
				{
					next = random.nextInt(16);
					item = mGame2048Items[next];
				}

				item.setNumber(Math.random() > 0.75 ? 4 : 2);

				isMergeHappen = isMoveHappen = false;
			}

		}
	}

	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restart()
	{
		for (Game2048Item item : mGame2048Items)
		{
			item.setNumber(0);
		}
		mScore = 0;
		if (mGame2048Listener != null)
		{
			mGame2048Listener.onScoreChange(mScore);
		}
		isMoveHappen = isMergeHappen = true;
		generateNum();
	}


}
