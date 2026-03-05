package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    SUCCESS("SU", "Success"),
    NOT_FOUND_USER("NF", "Not Found User"),
    EXIST_USER("AE", "Already Exist User"),
    DUPLICATE_EMAIL("DE", "Duplicate Email"),
    UN_CORRECT_PASSWORD("UC", "UnCorrect Password"),
    NOT_FOUND_CERTIFICATION("NF", "Not Found Certification"),
    UN_CORRECT_CERTIFICATION("UC", "Un Correct Certification"),
    UN_VALID_TOKEN("UT", "Unvalid Token"),
    NOT_FOUND_ALARM("NFA", "Not Found Alarm!!"),
    NOT_FOUND_GATHERING("NFG", "Not Found Gathering!!"),
    NOT_FOUND_CATEGORY("NFC", "Not Found Category"),
    NOT_FOUND_MEETING("NF", "Not found meeting!!"),
    NOT_AUTHORIZE("NC", "Not authorized meeting!!"),
    ALREADY_ATTEND("AT", "Already Attend Meeting"),
    NOT_WITHDRAW("NW", "Not Withdraw Meeting"),
    NOT_FOUND_ATTEND("NF", "Not Found Attend"),
    ALREADY_ENROLLMENT("AE", "already enrolled!!"),
    NOT_FOUND_ENROLLMENT("NE", "Not Found Enrollment!!"),
    NOT_DIS_ENROLLMENT("ND", "Opener Cannot  Enrollment"),
    ALREADY_LIKE("AL", "Already Like"),
    NOT_FOUND_LIKE("NF", "No Found Like"),
    NOT_FOUND_BOARD("NB", "Not Found Board"),
    NOT_FOUND_CHAT_ROOM("NC", "NOT Found Chat Room"),
    NOT_FOUND_CHAT_PARTICIPANT("NC", "NOT Found Chat Participant"),
    NOT_FOUND_IMAGE("NF", "Not Found Image"),
    UPLOAD_FAIL("UF", "Upload Fail"),
    DB_ERROR("DE", "Database Error"),
    FAIL_MESSAGE("FM", "Fail Messaging From SMTP"),
    METHOD_NOT_ALLOWED("MN", "Method Not Allowed"),

    private final String code;
    private final String content;
}
