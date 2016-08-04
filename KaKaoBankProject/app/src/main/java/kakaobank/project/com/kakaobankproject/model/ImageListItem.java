package kakaobank.project.com.kakaobankproject.model;


/**
 * Image List Item Model Object
 * 
 * Created by sohee.park
 */
public class ImageListItem {

    private int id = -1;
    private String title; // 제목
    private String thumbnailUrl; // 썸네일 url
    private String thumbnailContent; // 썸네일 url로 가져온 비트맵을 base64로 변환한 데이터
    private String imageUrl; // 이미지 url
    private String link; // 링크
    
    
    public void setImageListItem(int id, String title, String thumbnailUrl, 
    		String thumbnailContent, String imageUrl, String link) {
    	setId(id);
        setTitle(title);
        setThumbnailUrl(thumbnailUrl);
        setThumbnailContent(thumbnailContent);
        setImageUrl(imageUrl);
        setLink(link);
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailContent() {
        return thumbnailContent;
    }

    public void setThumbnailContent(String thumbnailContent) {
        this.thumbnailContent = thumbnailContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
