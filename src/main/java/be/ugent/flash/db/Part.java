package be.ugent.flash.db;

import org.w3c.dom.Text;

public record Part(int part_id, int question_id, byte[] part) {
}
