package servico;

import java.util.ArrayList;
import java.util.List;

import dao.ArtistaDao;
import dao.DaoFactory;
import dao.Transaction;
import dominio.Artista;

public class ArtistaServico {

	private ArtistaDao dao;
	
	public ArtistaServico() {
		dao = DaoFactory.criarArtistaDao();
	}
	
	public void validar(Artista x) throws ValidacaoException {
		List<String> erros = new ArrayList<>();
		
		if (x.getNome()==null) {
			erros.add("Favor preencher o campo nome");
		}
		if (x.getNacionalidade()==null) {
			erros.add("Favor preencher o campo nacionalidade");
		}
		if (x.getCache()==null) {
			erros.add("Favor preencher um valor v�lido para o cache");
		}
		if (x.getNascimento()==null) {
			erros.add("Favor preencher um valor v�lido para a data de nascimento");
		}
		
		if (!erros.isEmpty()) {
			throw new ValidacaoException("Erro de valida��o", erros);
		}
	}
	
	
	public void inserir(Artista x) throws ServicoException {
		try {
			Artista aux = dao.buscaNomeExato(x.getNome());
			if (aux != null) {
				throw new ServicoException("J� existe um artista com esse nome!", 1);
			}
			
			Transaction.begin();
			dao.inserirAtualizar(x);
			Transaction.commit();
		}
		catch (RuntimeException e) {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void atualizar(Artista x) throws ServicoException {
		try {
			Artista aux = dao.buscaNomeExatoDiferente(x.getCodArtista(), x.getNome());
			if (aux != null) {
				throw new ServicoException("J� existe um artista com esse nome!", 1);
			}
			
			Transaction.begin();
			dao.inserirAtualizar(x);
			Transaction.commit();
		}
		catch (RuntimeException e) {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void excluir(Artista x) throws ServicoException {
		try {
			x = dao.buscar(x.getCodArtista());
			if (!x.getParticipacoes().isEmpty()) {
				throw new ServicoException("Exclus�o n�o permitida: este artista possui participa��es!", 2);
			}
			
			Transaction.begin();
			dao.excluir(x);
			Transaction.commit();
		}
		catch (RuntimeException e) {
			if (Transaction.isActive()) {
				Transaction.rollback();
			}
			System.out.println("Erro: " + e.getMessage());
		}
	}
	
	public Artista buscar(int cod) {
		return dao.buscar(cod);
	}
	
	public List<Artista> buscarTodos() {
		return dao.buscarTodos();
	}
	
	public List<Artista> buscarTodosOrdenadosPorNome() {
		return dao.buscarTodosOrdenadosPorNome();
	}
	
	public List<Artista> buscarPorNome(String trecho) {
		return dao.buscarPorNome(trecho);
	}
}
