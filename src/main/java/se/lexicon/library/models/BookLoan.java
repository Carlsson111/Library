package se.lexicon.library.models;

import java.time.LocalDate;

public class BookLoan {
    private int id;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;
    private AppUser borrower;
    private Book book;
}
