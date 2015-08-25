package cn.huiwei13.game2048;

import cn.huiwei13.game2048.View.Game2048Layout;
import cn.huiwei13.game2048.View.Game2048Layout.OnGame2048Listener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends Activity implements OnGame2048Listener
{
	private Game2048Layout mGame2048Layout;

	private TextView mScore;
	
	private Button restart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mScore = (TextView) findViewById(R.id.id_score);
		restart = (Button) findViewById(R.id.restart);
		mGame2048Layout = (Game2048Layout) findViewById(R.id.id_game2048);
		restart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mGame2048Layout.restart();
			}
		});
		mGame2048Layout.setOnGame2048Listener(this);
	}

	@Override
	public void onScoreChange(int score)
	{
		mScore.setText("SCORE: " + score);
	}

	@Override
	public void onGameOver()
	{
		new AlertDialog.Builder(this).setTitle("GAME OVER")
				.setMessage("YOU HAVE GOT " + mScore.getText())
				.setPositiveButton("RESTART", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						mGame2048Layout.restart();
					}
				}).setNegativeButton("EXIT", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				}).show();
	}

}