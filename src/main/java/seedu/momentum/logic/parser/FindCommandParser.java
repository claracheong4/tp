package seedu.momentum.logic.parser;

import static seedu.momentum.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.momentum.logic.parser.CliSyntax.FIND_TYPE;
import static seedu.momentum.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.momentum.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.momentum.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.momentum.logic.commands.FindCommand;
import seedu.momentum.logic.parser.exceptions.ParseException;
import seedu.momentum.model.project.Project;
import seedu.momentum.model.project.predicates.DescriptionContainsKeywordsPredicate;
import seedu.momentum.model.project.predicates.FindType;
import seedu.momentum.model.project.predicates.NameContainsKeywordsPredicate;
import seedu.momentum.model.project.predicates.TagListContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    public static final String FIND_ARGUMENT_DELIMITER = "\\s+";

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_TAG, FIND_TYPE);

        Prefix[] prefixesToParse = new Prefix[] {PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_TAG};

        if (!argMultimap.getPreamble().isEmpty() || !anyPrefixPresent(argMultimap, prefixesToParse)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        FindType findType = getMatchType(argMultimap); // only parses find type if the argument exists
        List<Predicate<Project>> predicateList = new ArrayList<>(); // list of all predicates that will be applied

        for (Prefix prefix : prefixesToParse) {
            parseArguments(argMultimap, prefix, predicateList, findType);
        }

        return new FindCommand(combinePredicates(findType, predicateList));
    }

    private Predicate<Project> combinePredicates(FindType findType, List<Predicate<Project>> predicateList) {

        BinaryOperator<Predicate<Project>> operationType;
        switch (findType) {
        case ALL:
            operationType = Predicate::and;
            break;
        case ANY:
            // Find any is the default type
            // Fallthrough
        default:
            operationType = Predicate::or;
            break;

        }
        return predicateList.stream().reduce(operationType).orElse(x -> true);
    }

    private void parseArguments (ArgumentMultimap argMultimap, Prefix prefix,
             List<Predicate<Project>> predicateList, FindType findType) throws ParseException {

        if (argMultimap.getValue(prefix).isEmpty()) {
            return;
        }

        String args = argMultimap.getValue(prefix).get();
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> keywords = Arrays.asList(trimmedArgs.split(FIND_ARGUMENT_DELIMITER));

        if (prefix.equals(PREFIX_NAME)) {
            predicateList.add(new NameContainsKeywordsPredicate(findType, keywords));
        } else if (prefix.equals(PREFIX_DESCRIPTION)) {
            predicateList.add(new DescriptionContainsKeywordsPredicate(findType, keywords));
        } else if (prefix.equals(PREFIX_TAG)) {
            predicateList.add(new TagListContainsKeywordsPredicate(findType, keywords));
        }
    }

    private FindType getMatchType(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(FIND_TYPE).isEmpty()) {
            return FindType.ANY;
        }

        String findTypeArgument = argMultimap.getValue(FIND_TYPE).get();
        findTypeArgument = findTypeArgument.trim();

        try {
            return FindType.valueOf(findTypeArgument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

    }

    /**
     * Returns false if all of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean anyPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}