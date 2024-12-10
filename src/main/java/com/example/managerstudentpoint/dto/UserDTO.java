package com.example.managerstudentpoint.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseAbstractDTO<UserDTO> {
    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Size(max = 20, min = 3, message = "Min size > 3 and max size <20")
    private String password;

    @Pattern(regexp = "^[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgG" +
            "hHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRs" +
            "StTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴ\\s]+$",
            message = "Full name is invalid! It must be letter!")
    @NotBlank(message = "Fullname is mandatory")
    private String fullName;

    @Size(max = 12, min = 8, message = "invalid phone number")
    @Pattern(regexp = "^[(84|0[3|5|7|8|9])+([0-9]{8})]+$")
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Email is invalid! Ex: abc@gmail.com!")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String gender;
    private String address;
    private String rollNumber;
    private GroupClassDTO groupClass;
    private Set<String> role;
    private Set<String> subject;
    private Boolean isDelete;

}
