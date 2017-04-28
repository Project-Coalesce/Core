package example;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoModule;
import com.coalesce.ttb.TextToBlock;
import com.coalesce.ttb.blocks.FontLoader;
import com.coalesce.ttb.config.FontsConfig;
import com.coalesce.ttb.gui.TextMenu;
import com.coalesce.ttb.session.SessionHolder;
import com.coalesce.ttb.session.TextSession;
import org.bukkit.ChatColor;

/**
 * Note: This does not need to extend CoModule
 */
public final class TTBCommands extends CoModule {

	public TTBCommands(CoPlugin plugin) {
		super(plugin, "Commands Module"); //Creates a module with the name of "Commands Module"
		
		CoCommand hiCommand = new CommandBuilder(plugin, "hi") //This is the CoCommand Builder. You will use this generated CoCommand (hiCommand) to register it in the server.
				.executor(this::hi) //Refrences the hi command method
				.usage("/hi")
				.description("Hello!")
				.permission("core.hi")
				.playerOnly()
				.build();
		
		CoCommand helloCommand = new CommandBuilder(plugin, "hello")
				.executor(this::hello)
				.maxArgs(0)
				.permission("core.hello")
				.usage("/hello")
				.description("Hi!")
				.playerOnly()
				.build();
		
		CoCommand leaveCommand = new CommandBuilder(plugin, "leave")
				.executor(this::leave)
				.maxArgs(0)
				.permission("core.leave")
				.usage("/leave")
				.description("Cya round!")
				.consoleOnly()
				.build();
		
		plugin.addCommand(hiCommand, helloCommand, leaveCommand);
	}
	
	@Override
	protected void onEnable() throws Exception {
		//You only need the onEnable and onDisable if this is a module.
	}
	
	@Override
	protected void onDisable() throws Exception {
		
	}
	
	public void hi(CommandContext context) {
		context.send("Hi there");
	}
	
	public void hello(CommandContext context) {
		context.send("Hello " + context.asPlayer().getName() + "!");
	}
	
	public void leave(CommandContext context) {
		context.send("You gotta go... Cya!");
	}
}