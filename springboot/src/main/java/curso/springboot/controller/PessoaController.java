package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	PessoaRepository repositorio ;
	
	@Autowired
	TelefoneRepository telefoneRepositorio;
	
	Pessoa pessoa = new Pessoa();

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		modelAndView.addObject("pessoas", repositorio.findAll());
		return modelAndView;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas", repositorio.findAll());
			modelAndView.addObject("pessoaobj", new Pessoa());
			List<String> msg = new ArrayList<String>();
			
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());// Vem das anotações
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;	
		}
		
		repositorio.save(pessoa);
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", repositorio.findAll());
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET ,  value ="/listapessoas")
	public ModelAndView pessoas() {
		
		ModelAndView modeloTela = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> it = repositorio.findAll();
		modeloTela.addObject("pessoas", it);
		modeloTela.addObject("pessoaobj", new Pessoa());
		return modeloTela;
	
	}
	
	//"nomeDoCaminho/{nomeDaVariavel}"
	@GetMapping(value = "editarpessoa/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Pessoa pessoa = repositorio.findById(id).get();
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", pessoa);
		return modelAndView;
		
	}
	
	@GetMapping(value = "excluirpessoa/{id}")
	public ModelAndView excluir(@PathVariable("id") Long id) {
		repositorio.deleteById(id);
		ModelAndView modelAndView =  new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", repositorio.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}
	
	@PostMapping("**/pesquisar")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomePesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", repositorio.pesquisarPorNome(nomePesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}
	
	
	@GetMapping(value="**/telefones/{id}")
	public ModelAndView telefones(@PathVariable("id") String id) {
		System.out.println(id); 
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", repositorio.findById(Long.parseLong(id)).get());
		modelAndView.addObject("telefones", telefoneRepositorio.telefonePessoa(Long.parseLong(id)));
		return modelAndView;
	}
	
	//Quando é via get, eu uso PathVariable
	//Quando é via post, eu uso RequestParam
	
	@PostMapping(value = "**/addfonePessoa/{id}")
	public ModelAndView addfonePessoa(Telefone telefone, @PathVariable("id") Long id) {
		
		if (telefone == null || telefone.getNumero() == null || 
				telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty() || telefone.getTipo()==null) {
			
			// Aqui ele vai fazer de tudo, menos salvar.
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("telefones", telefoneRepositorio.telefonePessoa(id));
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<>();
			msg.add("Numero deve ser informado");
			modelAndView.addObject("msg", msg);
			
			return modelAndView;
		}
		
		Pessoa pessoa = repositorio.findById(id).get();
		telefone.setPessoa(pessoa);
		telefoneRepositorio.save(telefone);
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("telefones", telefoneRepositorio.telefonePessoa(id));
		modelAndView.addObject("pessoaobj", pessoa);
		return modelAndView;
		
	}
	
	@GetMapping(value="exluirTelefone/{idTel}")
	public ModelAndView excluirTelefone(@PathVariable("idTel") Long id) {
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		Pessoa pessoaId = telefoneRepositorio.findById(id).get().getPessoa();
		modelAndView.addObject("pessoaobj", pessoaId);
		telefoneRepositorio.deleteById(id);
		modelAndView.addObject("telefones", telefoneRepositorio.telefonePessoa(pessoaId.getId()));
		return modelAndView;
	}
	
	
}