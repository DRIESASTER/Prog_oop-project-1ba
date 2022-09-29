package be.ugent.flash.db;

public record Question(int question_id, String title, String text_part, byte[] image_part, String question_type, String correct_answer) {
}
