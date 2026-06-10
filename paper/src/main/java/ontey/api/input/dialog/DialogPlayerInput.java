package ontey.api.input.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.*;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import lombok.Builder;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import ontey.api.input.PlayerInput;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A {@link PlayerInput} that queries input using minecraft's dialog system.
 * It has support for booleans, floats (number slider), single options (enums) and most importantly text.
 * If the types are not sufficient for you, you can create your own {@link InputCollectionType}
 * that uses a String input and parses your needed type from it.
 * <blockquote>
 * Here is an example for Integer:
 * <pre>{@code
 *    public static InputCollectionType<Integer> integer() {
 * 	   return new InputCollectionType<>(response -> {
 * 		   String result = response.getText(INPUT);
 *
 * 		   return Nullity.nonNullOr(result, Integer::parseInt);
 *      }, dialogTitle -> DialogInput.text(INPUT, dialogTitle).build());
 *    }
 *    }</pre>
 * </blockquote>
 */

@Builder
public record DialogPlayerInput<T>(Component dialogTitle, Component sendLabel, Component cancelLabel,
                                   InputCollectionType<T> collectionType) implements PlayerInput<T> {
	
	public static final Component
	  DEFAULT_DIALOG_TITLE = Component.text("Input"),
	  DEFAULT_SEND_LABEL = Component.text("Send", NamedTextColor.GREEN),
	  DEFAULT_CANCEL_LABEL = Component.text("Cancel", NamedTextColor.RED);
	
	private static final String INPUT = "input";
	
	public DialogPlayerInput(Component dialogTitle, InputCollectionType<T> collectionType) {
		this(dialogTitle, DEFAULT_SEND_LABEL, DEFAULT_CANCEL_LABEL, collectionType);
	}
	
	public DialogPlayerInput(Component dialogTitle, Component sendLabel, InputCollectionType<T> collectionType) {
		this(dialogTitle, sendLabel, DEFAULT_CANCEL_LABEL, collectionType);
	}
	
	public DialogPlayerInput(InputCollectionType<T> collectionType) {
		this(DEFAULT_DIALOG_TITLE, DEFAULT_SEND_LABEL, DEFAULT_CANCEL_LABEL, collectionType);
	}
	
	@NonNull
	public static InputCollectionType<Boolean> bool(@NonNull Function<BooleanDialogInput.@NonNull Builder, @NonNull BooleanDialogInput> builderFunction) {
		return new InputCollectionType<>(response -> response.getBoolean(INPUT), dialogTitle -> builderFunction.apply(DialogInput.bool(INPUT, dialogTitle)));
	}
	
	@NonNull
	public static InputCollectionType<Boolean> bool() {
		return bool(BooleanDialogInput.Builder::build);
	}
	
	@NonNull
	public static InputCollectionType<Float> numberRange(@NonNull Function<NumberRangeDialogInput.@NonNull Builder, @NonNull NumberRangeDialogInput> builderFunction, float start, float end) {
		return new InputCollectionType<>(response -> response.getFloat(INPUT), dialogTitle -> builderFunction.apply(DialogInput.numberRange(INPUT, dialogTitle, start, end)));
	}
	
	@NonNull
	public static InputCollectionType<Float> numberRange(float start, float end) {
		return numberRange(NumberRangeDialogInput.Builder::build, start, end);
	}
	
	@NonNull
	public static InputCollectionType<String> singleOption(@NonNull Function<SingleOptionDialogInput.@NonNull Builder, @NonNull SingleOptionDialogInput> builderFunction, List<SingleOptionDialogInput.OptionEntry> options) {
		return new InputCollectionType<>(response -> response.getText(INPUT), dialogTitle -> builderFunction.apply(DialogInput.singleOption(INPUT, dialogTitle, options)));
	}
	
	@NonNull
	public static InputCollectionType<String> singleOption(@NonNull List<SingleOptionDialogInput.OptionEntry> options) {
		return singleOption(SingleOptionDialogInput.Builder::build, options);
	}
	
	@NonNull
	public static InputCollectionType<String> text(@NonNull Function<TextDialogInput.Builder, @NonNull TextDialogInput> builderFunction) {
		return new InputCollectionType<>(response -> response.getText(INPUT), dialogTitle -> builderFunction.apply(DialogInput.text(INPUT, dialogTitle)));
	}
	
	@NonNull
	public static InputCollectionType<String> text() {
		return text(TextDialogInput.Builder::build);
	}
	
	@Override
	@NonNull
	public CompletableFuture<T> queryInput(Player player) {
		CompletableFuture<T> future = new CompletableFuture<>();
		
		Dialog dialog = Dialog.create(builder -> builder.empty()
		  .base(
			 DialogBase.builder(Component.text("input"))
				.inputs(
				  List.of(collectionType.input.apply(dialogTitle))
				)
				.build())
		  .type(
			 DialogType.confirmation(
				ActionButton.builder(sendLabel)
				  .action(DialogAction.customClick((response, _) ->
					 future.complete(collectionType.function.apply(response)), ClickCallback.Options.builder().build()
				  ))
				  .build(),
				
				ActionButton.builder(cancelLabel)
				  .action(DialogAction.customClick((_, _) ->
					 future.complete(null), ClickCallback.Options.builder().build()
				  ))
				  .build()
			 )
		  )
		);
		
		player.showDialog(dialog);
		
		return future;
	}
	
	public record InputCollectionType<T>(@NonNull Function<@NonNull DialogResponseView, @Nullable T> function,
	                                     @NonNull Function<@NonNull Component, @NonNull DialogInput> input) {
		
	}
}
