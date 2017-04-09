package com.coalesce.command.registry;

/**
 * Declares the method this is sitting on is a command.
 */
public @interface Cmd {
    
    /**
     * The maximum amount of arguments in the command before an error is thrown.
     * Default is -1 for no maximum amount.
     */
    int max() default -1;
    
    /**
     * The minimum amount of arguments in the command before an error is thrown.
     * Default is -1 for no minimum amount.
     */
    int min() default -1;
    
    /**
     * This tells the command handler that this command has a tab completion
     * method registered.
     */
    boolean completer() default false;
    
    /**
     * Specifies who is able to do the command. Default is Executor.ALL so
     * everyone can use the command.
     */
    Executor[] executor() default Executor.ALL;
    
    /**
     * The name of the command being executed.
     */
    String name();
    
    /**
     * The command description. Default is ""
     */
    String desc() default "";
    
    /**
     * The usage of the command. Default is ""
     */
    String usage() default "";
    
    /**
     * The command aliases. Default is new String[]{}
     */
    String[] aliases() default {};
    
    /**
     * Permission needed to use this command. Default is none.
     */
    String[] permissions() default {};
    
}
