package dmri.nopo;

public interface FilterManagerInterface {
	public String[] readLocalFilter();
	public boolean isInLocalFiter(String sms);
	public void writeLocalFilter(String sms);
	public void serverFilterWriter();
}
