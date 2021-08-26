package GongMoa.gongmoa.fileupload;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class UploadFile {

    @Id
    @GeneratedValue
    @Column(name = "uploadfile_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    public UploadFile() {
    }

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
