package

{{package-name}};

import io.noties.prism4j.languages.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class {{class-name}} implements GrammarLocator{

@SuppressWarnings("ConstantConditions")
private static final Grammar NULL=new Grammar("", new ArrayList<>()){
@NotNull
@Override
public String name(){
        return null;
        }

@NotNull
@Override
public List<Token>tokens(){
        return null;
        }
        };

private final HashMap<String, Grammar>cache=new HashMap<>(3);

@Nullable
@Override
public Grammar grammar(@NotNull Prism4j prism4j,@NotNull String language){

final String name=realLanguageName(language);

        Grammar grammar=cache.get(name);
        if(grammar!=null){
        if(NULL==grammar){
        grammar=null;
        }
        return grammar;
        }

        grammar=obtainGrammar(prism4j,name);
        if(grammar==null){
        cache.put(name,NULL);
        }else{
        cache.put(name,grammar);
        Grammar grammarExtended=triggerModify(prism4j,name);
        if(grammarExtended!=null){
        cache.put(name,grammarExtended);
        grammar=grammarExtended;
        }
        }

        return grammar;
        }

@NotNull
protected String realLanguageName(@NotNull String name){
        {{real-language-name}}
        }

@Nullable
protected Grammar obtainGrammar(@NotNull Prism4j prism4j,@NotNull String name){
        {{obtain-grammar}}
        }

protected Grammar triggerModify(@NotNull Prism4j prism4j,@NotNull String name){
        {{trigger-modify}}
        }

@Override
@NotNull
public HashSet<String> languages(){
        {{languages}}
        }
        }
