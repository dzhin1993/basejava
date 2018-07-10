package storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("C:\\Users\\User\\Desktop\\basejava\\src\\storage\\storage");

    protected Storage storage;

    private static final String UUID_1 = "uuid_1";
    private static final String UUID_2 = "uuid_2";
    private static final String UUID_3 = "uuid_3";
    private static final String UUID_4 = "uuid_4";
    private static final Resume R1 = new Resume(UUID_1, "Аня");
    private static final Resume R2 = new Resume(UUID_2, "Аня");
    private static final Resume R3 = new Resume(UUID_3, "Петя");
    private static final Resume R4 = new Resume(UUID_4, "Коля");

    static {
        R1.setContact(ContactType.PHONE, "1");
        R2.setContact(ContactType.PHONE, "2");
        R3.setContact(ContactType.PHONE, "3");
        R4.setContact(ContactType.PHONE, "4");
        R1.setContact(ContactType.MAIL, "1@mail.ru");
        R2.setContact(ContactType.MAIL, "2@mail.ru");
        R3.setContact(ContactType.MAIL, "3@mail.ru");
        R4.setContact(ContactType.MAIL, "4@mail.ru");
        R1.setSection(SectionType.PERSONAL, new TextSection("1"));
        R1.setSection(SectionType.PERSONAL, new TextSection("2"));
        R1.setSection(SectionType.PERSONAL, new TextSection("3"));
        R1.setSection(SectionType.PERSONAL, new TextSection("4"));
        R1.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2")));
        R2.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3")));
        R3.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3", "4")));
        R4.setSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList("1", "2", "3", "4", "5")));
        Company.Post post = new Company.Post("developer", 2016, Month.APRIL, 2017, Month.DECEMBER, "good");
        Company.Post post2 = new Company.Post("developer2", 2012, Month.MARCH, 2018, Month.DECEMBER, "good");
        Company company1 = new Company("good company", "company.com", post, post2);
        Company company2 = new Company("good company", "company.com", post, post2);
        R1.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R2.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R3.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
        R4.setSection(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(company1, company2)));
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R3);
        storage.save(R2);
        storage.save(R1);
    }


    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1, "Никита");
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistStorageException() {
        storage.update(R4);
    }

    @Test
    public void save() {
        storage.save(R4);
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorageException() {
        storage.save(R3);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void get() {
        assertEquals(R1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() {
        storage.get(UUID_4);
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorageException() {
        storage.delete(UUID_4);
    }

    @Test
    public void getAll() {
        List<Resume> actual = Arrays.asList(R1, R2, R3);
        assertThat(actual, is(storage.getAllSorted()));
    }
}