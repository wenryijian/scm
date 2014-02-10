public class subjectSelect
{
	public static int gradeSeven=1+2+4+32+64+128+256;	//语文、数学、英语、思品、历史、生物、地理
	public static int gradeEight=1+2+4+8+32+64;			//语文、数学、英语、物理、思品、历史
	public static int gradeNine=1+2+4+8+16+32+64;		//语文、数学、英语、物理、化学、思品、历史
	public static void main(String[] args)
	{
		int grade=gradeSeven;	//测试七年级的科目
		for(int subjectSeq=1;subjectSeq<=256;subjectSeq=subjectSeq*2)
		{
			if(grade>=subjectSeq)
			{
				System.out.println("科目序号="+subjectSeq);	//可以根据该序号查询对应的科目名称
				grade=grade-subjectSeq;
			}
		}
	}
}